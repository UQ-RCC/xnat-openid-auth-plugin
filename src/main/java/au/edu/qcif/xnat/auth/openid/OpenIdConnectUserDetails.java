/*
 *Copyright (C) 2018 Queensland Cyber Infrastructure Foundation (http://www.qcif.edu.au/)
 *
 *This program is free software: you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License along
 *with this program; if not, write to the Free Software Foundation, Inc.,
 *51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package au.edu.qcif.xnat.auth.openid;

import org.nrg.xdat.security.XDATUser;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.util.Map;

/**
 * OIDC user details
 * 
 * @author <a href='https://github.com/shilob'>Shilo Banihit</a>
 * 
 */
public class OpenIdConnectUserDetails extends XDATUser {

	private static final long serialVersionUID = 1L;
	private OAuth2AccessToken token;
	private String email;
	private Map<String, String> openIdUserInfo;
	private String name;
	private String picture;
	private String firstName;
	private String lastName;
	private String pw;
	private String username;
	private String providerId;
	private OpenIdAuthPlugin plugin;

	public OpenIdConnectUserDetails(String providerId, Map<String, String> userInfo, OAuth2AccessToken token,
			OpenIdAuthPlugin plugin) {
		this.openIdUserInfo = userInfo;
		this.providerId = providerId;
		this.token = token;
		this.plugin = plugin;

		this.setUsername(getUsername(userInfo));
		this.email = getUserInfo(userInfo, "emailProperty", "");
		this.setFirstname(getUserInfo(userInfo, "givenNameProperty", ""));
		this.setLastname(getUserInfo(userInfo, "familyNameProperty", ""));
		this.name = userInfo.get("name");
		this.picture = userInfo.get("picture");
	}

	private String getUserInfo(Map<String, String> userInfo, String propName, String defaultVal) {
		String propVal = userInfo.get(plugin.getProperty(providerId, propName));
		return propVal != null ? propVal : defaultVal;
	}

	private String getUsername(Map<String, String> userInfo) {
		String prop, idp, sub;
		prop = plugin.getProperty(providerId, "providerProperty");
		if (prop != null && userInfo.containsKey(prop)) {
			idp = userInfo.get(prop);
		} else {
			idp = providerId;
		}
		prop = plugin.getProperty(providerId, "providerSubProperty");
		if (prop != null && userInfo.containsKey(prop)) {
			sub = userInfo.get(prop);
		} else {
			sub = userInfo.get("sub");
		}
		return idp + "_" + sub;
        }

	public OAuth2AccessToken getToken() {
		return token;
	}

	public void setToken(OAuth2AccessToken token) {
		this.token = token;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public String getFirstname() {
		return firstName;
	}

	public String getLastname() {
		return lastName;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String e) {
		this.email = e;
	}

	public void setFirstname(String firstname) {
		this.firstName = firstname;
	}

	public void setLastname(String lastname) {
		this.lastName = lastname;
	}

}
