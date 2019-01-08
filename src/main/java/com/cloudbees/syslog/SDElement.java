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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A SD-ELEMENT
 * 
 * @author <a href="mailto:brett@thebergquistfamily.com">Brett Bergquist</a>
 */
public class SDElement implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Reserved SD-IDs as documented in <a href="https://www.rfc-editor.org/rfc/rfc5424.txt">RFC-5424</a>
     */
    private final List<String> RESERVED_SDID = Collections.unmodifiableList(Arrays.asList("timeQuality", "origin", "meta"));

    public SDElement(String sdID) {
        validateSDID(sdID);
        this.sdID = sdID;
    }
    
    public SDElement(String sdID, SDParam... sdParams) {
        validateSDID(sdID);
        this.sdID = sdID;
        this.sdParams.addAll(Arrays.asList(sdParams));
    }

    private String sdID;

    /**
     * Get the value of sdID
     *
     * @return the value of sdID
     */
    public String getSdID() {
        return sdID;
    }

    private List<SDParam> sdParams = new ArrayList<SDParam>();

    /**
     * Get the value of sdParams
     *
     * @return the value of sdParams
     */
    public List<SDParam> getSdParams() {
        return sdParams;
    }

    /**
     * Set the value of sdParams
     *
     * @param sdParams new value of sdParams
     */
    public void setSdParams(List<SDParam> sdParams) {
        if (null == sdParams) {
            throw new IllegalArgumentException("sdParams list cannot be null");
        }
        this.sdParams.addAll(sdParams);
    }

    /**
     * Adds a SDParam
     * @param paramName the PARAM-NAME
     * @param paramValue the PARAM-VALUE
     * @return
     */
    public SDElement addSDParam(String paramName, String paramValue) {
        return addSDParam(new SDParam(paramName, paramValue));
    }
    
    public SDElement addSDParam(SDParam sdParam) {
        this.sdParams.add(sdParam);
        return this;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.sdID);
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
        final SDElement other = (SDElement) obj;
        return Objects.equals(this.sdID, other.sdID);
    }
    
    private void validateSDID(String sdName) {
        if (null == sdName) {
            throw new IllegalArgumentException("SD-ID cannot be null");
        }
        if (sdName.length() > 32) {
            throw new IllegalArgumentException("SD-ID must be less than 32 characters: " + sdName);
        }
        if (sdName.contains("=")) {
            throw new IllegalArgumentException("SD-ID cannot contain '='");
        }
        if (sdName.contains(" ")) {
            throw new IllegalArgumentException("SD-ID cannot contain ' '");
        }
        if (sdName.contains("]")) {
            throw new IllegalArgumentException("SD-ID cannot contain ']'");
        }
        if (sdName.contains("\"")) {
            throw new IllegalArgumentException("SD-ID cannot contain '\"'");
        }
        if (! sdName.contains("@")) {
            boolean found = false;
            for (String rn : RESERVED_SDID) {
                if (rn.equals(sdName)) {
                    found = true;
                    break;
                }
            }
            if (! found) {
                throw new IllegalArgumentException("SD-ID is not known registered SDID: " + sdName);
            }
        }
    }

}
