/*
* Copyright (C) 2015 Steven Lewi
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

package com.fenlisproject.elf.core.data;

import java.io.Serializable;

public class SessionDataWrapper<T extends Serializable> implements Serializable {

    private final long createdAt;
    private final long expiredAt;
    private T data;

    public SessionDataWrapper(T data) {
        this.data = data;
        this.createdAt = System.currentTimeMillis();
        this.expiredAt = -1;
    }

    public SessionDataWrapper(T data, long lifeSpan) {
        this.data = data;
        this.createdAt = System.currentTimeMillis();
        this.expiredAt = createdAt + lifeSpan;
    }

    public T getData() {
        return data;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getExpiredAt() {
        return expiredAt;
    }

    public boolean isExpired() {
        long currentTime = System.currentTimeMillis();
        return expiredAt == -1 ? false : (currentTime - expiredAt) > 0;
    }
}
