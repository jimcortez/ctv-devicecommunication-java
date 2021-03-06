/*******************************************************************************
 * Copyright (c) 2012, Yahoo! Inc.
 * All rights reserved.
 *
 * Redistribution and use of this software in source and binary forms,
 * with or without modification, are permitted provided that the following
 * conditions are met:
 *
 *  * Redistributions of source code must retain the above
 *    copyright notice, this list of conditions and the
 *    following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above
 *    copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 *  * Neither the name of Yahoo! Inc. nor the names of its
 *    contributors may be used to endorse or promote products
 *    derived from this software without specific prior
 *    written permission of Yahoo! Inc.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package com.yahoo.connectedtv.ycommand;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Wraps an protocol create sessione command
 * <p>
 * Example Command:
 * 
 * <pre>
 * {@literal
 * SESSION|CREATE|a44520388013d49fcdb16a6b7129bffb4e54ce99|Jim's Awesome App|END
 * }
 * </pre>
 * 
 * </p>
 * 
 * @author jecortez
 * 
 * @version $Revision: 1.0 $
 */
public class CreateSessionCommand extends AbstractSessionCommand {
	/**
	 * Field serialVersionUID. (value is -7356217426030331170)
	 */
	private static final long serialVersionUID = -7356217426030331170L;
	/**
	 * Field name.
	 */
	String name;
	private String appId;
	private String consumerKey;
	private String secret;

	/**
	 * @param appId
	 *            Application ID generated by Yahoo!
	 * @param consumerKey
	 *            Consumer Key generated by Yahoo!
	 * @param secret
	 *            Consumer Secret generated by Yahoo!
	 * @param name
	 *            unique human-readable name of this app/user
	 */
	public CreateSessionCommand(String appId, String consumerKey, String secret, String name) {
		super("SESSION");
		this.appId = appId;
		this.consumerKey = consumerKey;
		this.secret = secret;
		this.name = name;
	}

	/**
	 * Method toCommandString.
	 * 
	 * @return String
	 */
	@Override
	public String toCommandString() {
		return String.format("SESSION|CREATE|%s|%s|END", this.getAppKey(), this.name);
	}

	@Override
	public String getPayload() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * Method getAppKey.
	 * 
	 * @return String
	 */
	public String getAppKey() {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("app_id=");
			sb.append(URLEncoder.encode(this.appId, "UTF-8"));

			sb.append("&consumer_key=");
			sb.append(URLEncoder.encode(this.consumerKey, "UTF-8"));

			String secret = "";
			try {
				SecretKeySpec key = new SecretKeySpec(this.secret.getBytes("UTF-8"), "HmacSHA1");
				Mac mac = Mac.getInstance("HmacSHA1");
				mac.init(key);

				secret = this.byteArrayToHex(mac.doFinal(this.consumerKey.getBytes("UTF-8")));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			sb.append("&secret=");
			sb.append(URLEncoder.encode(secret, "UTF-8"));

		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		return sb.toString();
	}

	/**
	 * Method byteArrayToHex.
	 * 
	 * @param a
	 *            byte[]
	 * @return String
	 */
	private String byteArrayToHex(byte[] a) {
		int hn, ln, cx;
		String hexDigitChars = "0123456789abcdef";
		StringBuffer buf = new StringBuffer(a.length * 2);
		for (cx = 0; cx < a.length; cx++) {
			hn = ((int) (a[cx]) & 0x00ff) / 16;
			ln = ((int) (a[cx]) & 0x000f);
			buf.append(hexDigitChars.charAt(hn));
			buf.append(hexDigitChars.charAt(ln));
		}
		return buf.toString();
	}

	/**
	 * Method getName.
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Method setName.
	 * 
	 * @param name
	 *            String
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getConsumerKey() {
		return consumerKey;
	}

	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

}
