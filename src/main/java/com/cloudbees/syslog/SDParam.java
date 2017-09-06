/*
 * Copyright 2010-2014, CloudBees Inc.
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
package com.cloudbees.syslog;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author <a href="mailto:brett@thebergquistfamily.com">Brett Bergquist</a>
 */
public class SDParam implements Serializable {

    private static final long serialVersionUID = 1L;

    public SDParam(String paramName, String paramValue) {
        validateParamName(paramName);
        this.paramName = paramName;
        this.paramValue = paramValue;
    }
    
    private String paramName;

    /**
     * Get the value of paramName
     *
     * @return the value of paramName
     */
    public String getParamName() {
        return paramName;
    }

    /**
     * Set the value of paramName
     *
     * @param paramName new value of paramName
     */
    public void setParamName(String paramName) {
        validateParamName(paramName);
        this.paramName = paramName;
    }

    private String paramValue;

    /**
     * Get the value of paramValue
     *
     * @return the value of paramValue
     */
    public String getParamValue() {
        return paramValue;
    }

    /**
     * Set the value of paramValue
     *
     * @param paramValue new value of paramValue
     */
    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    private void validateParamName(String sdName) {
        if (null == sdName) {
            throw new IllegalArgumentException("PARAM-NAME cannot be null");
        }
        if (sdName.length() > 32) {
            throw new IllegalArgumentException("PARAM-NAME must be less than 32 characters: " + sdName); 
        }
        if (sdName.contains("=")) {
            throw new IllegalArgumentException("PARAM-NAME cannot contain '='");
        }
        if (sdName.contains(" ")) {
            throw new IllegalArgumentException("PARAM-NAME cannot contain ' '");
        }
        if (sdName.contains("]")) {
            throw new IllegalArgumentException("PARAM-NAME cannot contain ']'");
        }
        if (sdName.contains("\"")) {
            throw new IllegalArgumentException("PARAM-NAME cannot contain '\"'");
        }
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.paramName);
        hash = 59 * hash + Objects.hashCode(this.paramValue);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SDParam other = (SDParam) obj;
        if (!Objects.equals(this.paramName, other.paramName)) {
            return false;
        }
        if (!Objects.equals(this.paramValue, other.paramValue)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SDParam{" + "paramName=" + paramName + ", paramValue=" + paramValue + '}';
    }

}
