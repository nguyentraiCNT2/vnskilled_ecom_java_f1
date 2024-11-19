package vnskilled.edu.ecom.Model.Request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;
import java.time.Instant;

public class RequestContext {
    private static final ThreadLocal<RequestContext> context = new ThreadLocal<>();
    private String requestId;
    private Long userId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Timestamp timestamp;
    private String ipAddress;
    private String hostName;
    private String userAgent;
    private String requestURL;
    private String method;
    private int responseStatus;
    private String message;
    private String role;

    public RequestContext() {}

    public static RequestContext get() {
        return context.get();
    }

    public static void set(RequestContext requestContext) {
        context.set(requestContext);
    }

    public static void clear() {
        context.remove();
    }

    // Getters and Setters
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

	public Timestamp getTimestamp () {
		return timestamp;
	}

	public void setTimestamp (Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(int responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
