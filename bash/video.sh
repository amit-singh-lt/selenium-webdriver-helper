#!/bin/bash
# shellcheck disable=SC2059

# Configuration
API_BASE_URL="api.lambdatest.com/automation/api/v1/sessions"
VIDEO_URL_FORMAT="https://%s:%s@%s/%s/video"
SESSION_API="https://%s:%s@api.lambdatest.com/automation/api/v1/sessions/%s"
TEST_API="https://%s:%s@api.lambdatest.com/api/v1/test/%s"
INDEX_M3U8="https://automation-artefacts.lambdatest.com/orgId-%s/%s/%s/video/index.m3u8"
MAX_RETRIES=24
RETRY_DELAY=10  # seconds

# Declare global variables
response=""        # Holds the response body from API calls
response_log=""    # Holds log messages
video_duration=""  # Duration of the video fetched from ffprobe
test_duration=""   # Duration of the test fetched from the API
test_id=""         # Test ID extracted from the API responses

# Function to append messages to the global response_log and print them
log_message() {
    local message="$1"
    response_log+="$message\n"
    echo "$message"
}

# Function to fetch video data from the API
fetch_video_data() {
    local session_id="$1"
    local user_name="$2"
    local access_key="$3"
    local video_url

    # Format the video URL using provided user credentials and session ID
    video_url=$(printf "$VIDEO_URL_FORMAT" "$user_name" "$access_key" "$API_BASE_URL" "$session_id")
    log_message "Fetching video data from URL: $video_url"

    local attempt=0
    while [ $attempt -lt $MAX_RETRIES ]; do
        # Fetch response and status code using process substitution
        response=$(curl -s -w "%{http_code}" -o /dev/stdout "$video_url")
        http_code="${response: -3}"
        response_body="${response:0:${#response}-3}"

        log_message "Response Code: $http_code"

        if [ "$http_code" -eq 200 ] || [ "$http_code" -eq 304 ]; then
            response="$response_body"  # Save the response body for further use
            return
        else
            log_message "Retrying in $RETRY_DELAY seconds..."
            sleep $RETRY_DELAY
        fi

        attempt=$((attempt + 1))
    done

    log_message "Failed to fetch video data after $((MAX_RETRIES * RETRY_DELAY)) seconds"
    exit 1
}

# Function to check if the video is playable
check_video_playable() {
    local video_url="$1"

    log_message "Checking if video is playable at URL: $video_url"

    local attempt=0
    local response_code

    while [ $attempt -lt $MAX_RETRIES ]; do
        # Fetch HTTP status code for video URL
        response_code=$(curl -s -o /dev/null -w "%{http_code}" "$video_url")
        log_message "Video Public Response Code: $response_code"

        if [ "$response_code" -eq 200 ] || [ "$response_code" -eq 304 ]; then
            log_message "Video is playable."
            return 0
        else
            log_message "Retrying in $RETRY_DELAY seconds..."
            sleep $RETRY_DELAY
        fi

        attempt=$((attempt + 1))
    done

    log_message "Video is not playable after $((MAX_RETRIES * RETRY_DELAY)) seconds"
    return 1
}

# Function to get video duration using ffprobe
get_video_duration() {
    local video_url="$1"

    log_message "Fetching video duration from URL: $video_url"
    # Fetch video duration using ffprobe and remove carriage returns
    video_duration=$(ffprobe -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 "$video_url" | tr -d '\r')
    log_message "Video Duration is: $video_duration seconds"
}

# Function to get test duration from the API
get_test_duration() {
    local session_id="$1"
    local user_name="$2"
    local access_key="$3"

    session_api=$(printf "$SESSION_API" "$user_name" "$access_key" "$session_id")
    log_message "Session API URL: $session_api"

    session_api_response=$(curl -s "$session_api")
    log_message "Session API Response: $session_api_response"

    test_id=$(echo "$session_api_response" | jq -r '.data.test_id')
    test_duration=$(echo "$session_api_response" | jq -r '.data.duration')
    log_message "Test ID: $test_id"
    log_message "Test Duration: $test_duration seconds"
}

# Function to check the duration of the video and the test
check_video_duration() {
    local session_id="$1"
    local user_name="$2"
    local access_key="$3"
    local video_url="$4"

    log_message "Checking video duration for session ID: $session_id"
    # Get the video duration and the test duration
    get_video_duration "$video_url"
    get_test_duration "$session_id" "$user_name" "$access_key"

    local duration_diff
    duration_diff=$(echo "$video_duration - $test_duration" | bc)

    # Check if the difference exceeds the permissible limit of 5 seconds
    local permissible_diff=5
    if (( $(echo "$duration_diff > $permissible_diff" | bc -l) )); then
        log_message "Duration mismatch: Video Duration = $video_duration seconds, Test Duration = $test_duration seconds, Difference = $duration_diff seconds. Exceeds permissible limit of $permissible_diff seconds."
        exit 1
    else
        log_message "Duration check passed: Video Duration = $video_duration seconds, Test Duration = $test_duration seconds, Difference = $duration_diff seconds."
    fi
}

check_endlist() {
    index_m3u8_url=$(printf "$INDEX_M3U8" "$org_id" "$date" "$test_id")
    log_message "Fetching index.m3u8 from URL: $index_m3u8_url"
    response=$(curl -s "$index_m3u8_url")
    log_message "Index.m3u8 Response: $response"
    echo "$response" | grep -q "#EXT-X-ENDLIST"
}

# Function to check if the index.m3u8 file is correctly formatted
check_index_m3u8_for_sync() {
    local user_name="$1"
    local access_key="$2"

    test_api_url=$(printf "$TEST_API" "$user_name" "$access_key" "$test_id")
    log_message "Test API URL: $test_api_url"

    org_id=$(curl -s "$test_api_url" | jq -r '.org_id')
    log_message "Organization ID: $org_id"

    date=$(date -u +"%Y/%m/%d")
    log_message "Date: $date"

    local attempt=0
    while [ $attempt -lt $MAX_RETRIES ]; do
        if check_endlist; then
            log_message "#EXT-X-ENDLIST found"
            exit 0
        else
            log_message "Retrying in $RETRY_DELAY seconds..."
            sleep $RETRY_DELAY
        fi

        attempt=$((attempt + 1))
    done

    log_message "Fetching Video index.m3u8 data from URL: $index_m3u8_url"

    index_m3u8_url_response=$(curl -s "$index_m3u8_url")
    log_message "IndexM3U8 Response :- $index_m3u8_url_response"

    # Define an array of required lines to check
    required_lines_in_index_m3u8=(
        "#EXTM3U"
        "#START_TIMESTAMP"
        "#EXT-X-START:TIME-OFFSET"
        "#EXT-X-VERSION"
        "#EXT-X-TARGETDURATION"
        "#EXT-X-MEDIA-SEQUENCE"
        "#EXTINF"
        "#EXT-X-ENDLIST"
    )

    # Initialize a flag to track if any lines are missing
    missing_found=0

    # Check each required line
    for line in "${required_lines_in_index_m3u8[@]}"; do
        if echo "$index_m3u8_url_response" | grep -qF "$line"; then
            log_message "Found: $line"
        else
            log_message "Missing: $line"
            missing_found=1
        fi
    done

    # Check if index0.ts is present
    if echo "$index_m3u8_url_response" | grep -q "index0.ts"; then
        log_message "index0.ts is present"
    else
        log_message "index0.ts is missing"
        missing_found=1
    fi

    # Exit with a non-zero status if any lines were missing
    if [ $missing_found -eq 1 ]; then
        exit 1
    fi

    log_message "All checks passed successfully."
}

# Function to verify the test video
verify_test_video() {
    local session_id="$1"
    local user_name="$2"
    local access_key="$3"

    # Fetch video data from API
    fetch_video_data "$session_id" "$user_name" "$access_key"

    # Extract video URL from response
    local video_url_from_response
    video_url_from_response=$(echo "$response" | jq -r '.url')

    log_message "Extracted Video URL: $video_url_from_response"

    # Check if video URL is present
    if [ -z "$video_url_from_response" ]; then
        log_message "Video URL is not present in the response."
        exit 1
    fi

    # Check if the video is playable
    if check_video_playable "$video_url_from_response"; then
        log_message "Video is generated and playable."
    else
        log_message "Video is not playable after several retries."
        exit 1
    fi

    # Check video duration against test duration
    check_video_duration "$session_id" "$user_name" "$access_key" "$video_url_from_response"

    # Check the index.m3u8 file for the correct format
    check_index_m3u8_for_sync "$user_name" "$access_key"
}

# Ensure that all required parameters are provided
if [ $# -ne 3 ]; then
    log_message "Usage: $0 <session_id> <user_name> <access_key>"
    exit 1
fi

# Retrieve arguments
session_id="$1"
user_name="$2"
access_key="$3"

# Call the main function to verify the video
verify_test_video "$session_id" "$user_name" "$access_key"

# Output the collected log messages
echo -e "$response_log"
