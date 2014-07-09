/**
 * Copyright 2013-2014 Amazon.com, 
 * Inc. or its affiliates. All Rights Reserved.
 * 
 * Licensed under the Amazon Software License (the "License"). 
 * You may not use this file except in compliance with the 
 * License. A copy of the License is located at
 * 
 *     http://aws.amazon.com/asl/
 * 
 * or in the "license" file accompanying this file. This file is 
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, express or implied. See the License 
 * for the specific language governing permissions and 
 * limitations under the License.
 */

package com.amazonaws.android.cognito;

/**
 * This consists of the conflicting record from the remote storage and the local
 * storage.
 */
public class SyncConflict {
    private final String key;
    private final Record remoteRecord;
    private final Record localRecord;

    /**
     * Constructs a SyncConflict object.
     * 
     * @param remoteRecord record from remote storage
     * @param localRecord record from local storage
     */
    public SyncConflict(Record remoteRecord, Record localRecord) {
        if (remoteRecord == null || localRecord == null) {
            throw new IllegalArgumentException("record can't be null");
        }
        if (!remoteRecord.getKey().equals(localRecord.getKey())) {
            throw new IllegalArgumentException(
                    "the keys of remote record and local record don't match");
        }
        this.key = remoteRecord.getKey();
        this.remoteRecord = remoteRecord;
        this.localRecord = localRecord;
    }

    /**
     * Gets the key of the record that is in conflict.
     * 
     * @return key of the record
     */
    public String getKey() {
        return key;
    }

    /**
     * Gets the remote record that is in conflict.
     * 
     * @return record from remote storage
     */
    public Record getRemoteRecord() {
        return remoteRecord;
    }

    /**
     * Gets the local record that is in conflict.
     * 
     * @return record from local storage
     */
    public Record getLocalRecord() {
        return localRecord;
    }

    /**
     * Resolves conflict with remote record
     * 
     * @return resolved record
     */
    public Record resolveWithRemoteRecord() {
        return new Record.Builder(key)
                .value(remoteRecord.getValue())
                .syncCount(remoteRecord.getSyncCount())
                .lastModifiedDate(remoteRecord.getLasteModifiedDate())
                .lastModifiedBy(remoteRecord.getLastModifiedBy())
                .deviceLastModifiedDate(remoteRecord.getDeviceLastModifiedDate())
                .build();
    }

    /**
     * Resolves conflict with local record
     * 
     * @return resolved record
     */
    public Record resolveWithLocalRecord() {
        return new Record.Builder(key)
                .value(localRecord.getValue())
                .syncCount(remoteRecord.getSyncCount())
                .lastModifiedDate(localRecord.getLasteModifiedDate())
                .lastModifiedBy(localRecord.getLastModifiedBy())
                .deviceLastModifiedDate(localRecord.getDeviceLastModifiedDate())
                .modified(true)
                .build();
    }

    /**
     * Resolves conflict with a new value.
     * 
     * @param newValue new value of the record
     * @return resolved record
     */
    public Record resolveWithValue(String newValue) {
        return new Record.Builder(key)
                .value(newValue)
                .syncCount(remoteRecord.getSyncCount())
                .lastModifiedDate(localRecord.getLasteModifiedDate())
                .lastModifiedBy(localRecord.getLastModifiedBy())
                .deviceLastModifiedDate(localRecord.getDeviceLastModifiedDate())
                .modified(true)
                .build();
    }
}