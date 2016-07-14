/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yanxiu.basecore.bean;

import com.yanxiu.basecore.utils.BaseCoreLogInfo;

import java.io.IOException;

/**
 * Default retry policy for requests.
 */
public class YanxiuRetryPolicy {
    /** The current timeout in milliseconds. */
    private int mCurrentTimeoutMs;

    /** The current retry count. */
    private int mCurrentRetryCount;

    /** The maximum number of attempts. */
    private int mMaxNumRetries;

    /** The backoff multiplier for the policy. */
    private final float mBackoffMultiplier;

    /** The default socket timeout in milliseconds */
    public static final int DEFAULT_TIMEOUT_MS = 8000;

    /** The default number of retries
     * modified by zengsonghai at 20150518-15:44
     * 修改默认重试次数为3次
     * */
    public static final int DEFAULT_MAX_RETRIES = 3; //

    /** The default backoff multiplier */
    public static final float DEFAULT_BACKOFF_MULT = 0.5f;

    /**
     * Constructs a new retry policy using the default timeouts.
     */
    public YanxiuRetryPolicy() {
        this(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT);
    }

    /**
     * Constructs a new retry policy.
     * @param initialTimeoutMs The initial timeout for the policy.
     * @param maxNumRetries The maximum number of retries.
     * @param backoffMultiplier Backoff multiplier for the policy.
     */
    public YanxiuRetryPolicy(int initialTimeoutMs, int maxNumRetries, float backoffMultiplier) {
        mCurrentTimeoutMs = initialTimeoutMs;
        mMaxNumRetries = maxNumRetries;
        mBackoffMultiplier = backoffMultiplier;
    }

    /**
     * Returns the current timeout.
     */
    public int getCurrentTimeout() {
        BaseCoreLogInfo.err("mCurrentTimeoutMs =" + mCurrentTimeoutMs);
        return mCurrentTimeoutMs;
    }

    /**
     * Returns the current retry count.
     */
    public int getCurrentRetryCount() {
        return mCurrentRetryCount;
    }
    public void setMaxNumRetries(int mMaxNumRetries){
        this.mMaxNumRetries = mMaxNumRetries;
    }
    /**
     * Returns the backoff multiplier for the policy.
     */
    public float getBackoffMultiplier() {
        return mBackoffMultiplier;
    }

    /**
     * Prepares for the next retry by applying a backoff to the timeout.
     * @param error The error code of the last attempt.
     */
    public void retry(IOException error) throws IOException {
        mCurrentRetryCount++;
        BaseCoreLogInfo.err("mCurrentRetryCount=" + mCurrentRetryCount + " mCurrentTimeoutMs=" + mCurrentTimeoutMs);
        mCurrentTimeoutMs += (mCurrentTimeoutMs * mBackoffMultiplier);
        if (!hasAttemptRemaining()) {
            throw error;
        }
    }

    /**
     * Returns true if this policy has attempts remaining, false otherwise.
     */
    public boolean hasAttemptRemaining() {
        return mCurrentRetryCount < mMaxNumRetries;
    }
}
