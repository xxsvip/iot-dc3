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

package com.dc3.driver.service.impl;

import com.dc3.common.bean.driver.AttributeInfo;
import com.dc3.common.constant.Common;
import com.dc3.common.model.Device;
import com.dc3.common.model.Point;
import com.dc3.common.sdk.bean.DriverContext;
import com.dc3.common.sdk.service.DriverCustomService;
import com.dc3.common.sdk.service.DriverService;
import com.dc3.driver.service.mqtt.MqttSendHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

import static com.dc3.common.sdk.util.DriverUtils.attribute;

/**
 * @author pnoker
 */
@Slf4j
@Service
public class DriverCustomServiceImpl implements DriverCustomService {

    @Resource
    private DriverContext driverContext;
    @Resource
    private DriverService driverService;
    @Resource
    private MqttSendHandler mqttSendHandler;

    @Override
    public void initial() {
    }

    @Override
    public String read(Map<String, AttributeInfo> driverInfo, Map<String, AttributeInfo> pointInfo, Device device, Point point) throws Exception {
        return "nil";
    }

    @Override
    public Boolean write(Map<String, AttributeInfo> driverInfo, Map<String, AttributeInfo> pointInfo, Device device, AttributeInfo values) throws Exception {
        String commandTopic = attribute(pointInfo, "commandTopic"), value = values.getValue();
        try {
            int commandQos = attribute(pointInfo, "commandQos");
            mqttSendHandler.sendToMqtt(commandTopic, commandQos, value);
        } catch (Exception e) {
            mqttSendHandler.sendToMqtt(commandTopic, value);
        }
        return true;
    }

    @Override
    public void schedule() {

        /*
        TODO:????????????
        ???????????????????????????????????????????????????????????????schedule()??????????????????????????????read?????????????????????????????????
        ?????????????????????????????????????????????????????????????????????driverService.deviceEventSender???????????????????????????SDK?????????

        ???????????????DeviceStatus????????????
        ONLINE:??????
        OFFLINE:??????
        MAINTAIN:??????
        FAULT:??????
         */
        driverContext.getDriverMetadata().getDeviceMap().keySet().forEach(id -> driverService.deviceEventSender(id, Common.Device.Event.HEARTBEAT, Common.Device.Status.ONLINE));
    }

}
