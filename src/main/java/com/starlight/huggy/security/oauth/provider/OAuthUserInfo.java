package com.starlight.huggy.security.oauth.provider;

public interface OAuthUserInfo {
	String getProviderId();
	String getProvider();
	String getEmail();
	String getName();
}
