/*
   Copyright 2014 base2Services

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.base2.example.war.rest;

import com.base2.kagura.rest.AuthRest;
import com.base2.kagura.rest.exceptions.AuthenticationException;
import com.base2.kagura.rest.model.AuthenticationResult;
import com.base2.kagura.rest.model.ReportDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import java.io.Serializable;
import java.util.*;

/**
 * @author aubels
 *         Date: 26/08/13
 */
@Path("/auth")
@RequestScoped
public class AuthRestImpl extends AuthRest implements Serializable
{
    private static final Logger LOG = LoggerFactory.getLogger(AuthRestImpl.class);

    @Inject
    KaguraBean kaguraBean;

    @Override
    public AuthenticationResult getAuthToken(String user, String password)
    {
        try {
            kaguraBean.authenticateUser(user,password);
        } catch (Exception e) {
            LOG.info("Login error", e);
            return new AuthenticationResult()
            {{
                setError("Bad username / password");
            }};
        }
        kaguraBean.setUser(kaguraBean.getUserModel(user));
        return new AuthenticationResult()
        {{
                setError("");
                setToken(UUID.randomUUID().toString()); // UUID doesn't matter.
        }};
    }

    @Override
    public String testAuthToken(String authToken)
    {
        if (kaguraBean.getUser() == null)
        {
            return "Not OK";
        }
        return "Ok";
    }

    @Override
    public String logout(String authToken)
    {
        if (kaguraBean.getUser() == null)
        {
            return "Not done";
        }
        kaguraBean.setUser(null);
        return "Done";
    }

    @Override
    public List<String> getReports(String authToken)
    {
        if (kaguraBean.getUser() == null) throw new AuthenticationException("Authentication failure");
        return new ArrayList<String>()
        {{
            addAll(kaguraBean.getUserReports());
        }};
    }

    @Override
    public Map<String, ReportDetails> getReportsDetailed(final String authToken)
    {
        if (kaguraBean.getUser() == null) throw new AuthenticationException("Authentication failure");
        return new HashMap<String, ReportDetails>()
        {{
            for (String reportName : kaguraBean.getUserReports())
            {
                put(reportName, kaguraBean.getReportDetails(reportName, false, new ReportDetails()));
            }
        }};
    }
}
