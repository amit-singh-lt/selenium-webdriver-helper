package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class SessionDetailsDTO {
    public String id;
    public String name;
    @JsonProperty("org_id")
    public int orgId;
    public String username;
    public String remark;
    @JsonProperty("session_id")
    public String sessionId;
    public int buildId;
    public String buildName;
    @JsonProperty("hub_proxy_region")
    public String hubProxyRegion;
    @JsonProperty("hub_proxy_region_initial")
    public String hubProxyRegionInitial;
    public int commandCount;
    @JsonProperty("feature_flag")
    public Map<String, String> featureFlag;
    public String tunnelIdentifier;
    public Date createdAt;
    public Date startedAt;
    public Object endedAt;
    public int issues;
    public String status;
    public String statusInd;
    public String device;
    public String browser;
    public String version;
    public String resolution;
    public String seleniumVersion;
    public String appiumVersion;
    public String driverVersion;
    public String osId;
    public String osName;
    public String osVersion;
    public String category;
    public String capability;
    @JsonProperty("customdata_capabilty")
    public String customDataCapabilty;
    @JsonProperty("vm_geo_info")
    public String vmGeoInfo;
    @JsonProperty("group_id")
    public int groupId;
    public Object annotations;
    public Object tags;
}
