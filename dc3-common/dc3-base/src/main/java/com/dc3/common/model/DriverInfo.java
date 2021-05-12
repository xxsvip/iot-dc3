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

package com.dc3.common.model;

import com.dc3.common.valid.Insert;
import com.dc3.common.valid.Update;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 驱动配置信息表
 *
 * @author pnoker
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DriverInfo extends Description {

    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message = "driver attribute id can't be empty", groups = {Insert.class, Update.class})
    private Long driverAttributeId;

    private String value;

    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message = "profile id can't be empty", groups = {Insert.class, Update.class})
    private Long profileId;

}
