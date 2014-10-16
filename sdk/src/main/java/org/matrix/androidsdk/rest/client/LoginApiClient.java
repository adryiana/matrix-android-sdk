/* 
 * Copyright 2014 OpenMarket Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.matrix.androidsdk.rest.client;

import android.net.Uri;

import com.google.gson.JsonObject;

import org.matrix.androidsdk.MXApiClient;
import org.matrix.androidsdk.rest.ApiCallback;
import org.matrix.androidsdk.rest.api.LoginApi;
import org.matrix.androidsdk.rest.model.login.Credentials;
import org.matrix.androidsdk.rest.model.login.PasswordLoginParams;

import retrofit.RestAdapter;
import retrofit.client.Response;

/**
 * Class used to make requests to the login API.
 */
public class LoginApiClient extends MXApiClient {

    private LoginApi mApi;

    /**
     * Public constructor.
     * @param hsUri the home server URI
     */
    public LoginApiClient(Uri hsUri) {
        super(hsUri);
    }

    @Override
    protected void initApi(RestAdapter restAdapter) {
        mApi = restAdapter.create(LoginApi.class);
    }

    /**
     * Protected setter for injection by unit tests.
     * @param api the api object
     */
    protected void setApi(LoginApi api) {
        mApi = api;
    }

    /**
     * Attempt a user/password log in.
     * @param user the user name
     * @param password the password
     * @param callback the callback success and failure callback
     */
    public void loginWithPassword(String user, String password, final ApiCallback<Credentials> callback) {
        PasswordLoginParams params = new PasswordLoginParams();
        params.user = user;
        params.password = password;

        mApi.login(params, new ConvertFailureCallback<JsonObject>(callback) {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                mCredentials = gson.fromJson(jsonObject, Credentials.class);
                callback.onSuccess(mCredentials);
            }
        });
    }
}
