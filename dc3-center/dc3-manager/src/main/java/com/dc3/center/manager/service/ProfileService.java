/*
 * Copyright 2016-2021 Pnoker. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dc3.center.manager.service;

import com.dc3.common.base.Service;
import com.dc3.common.dto.ProfileDto;
import com.dc3.common.model.Profile;

import java.util.List;

/**
 * <p>Profile Interface
 *
 * @author pnoker
 */
public interface ProfileService extends Service<Profile, ProfileDto> {
    /**
     * 根据模板 NAME 查询
     *
     * @param name
     * @return Profile
     */
    Profile selectByName(String name);

    /**
     * 根据驱动 ID 查询
     *
     * @param driverId driver id
     * @return Profile Array
     */
    List<Profile> selectByDriverId(Long driverId);
}
