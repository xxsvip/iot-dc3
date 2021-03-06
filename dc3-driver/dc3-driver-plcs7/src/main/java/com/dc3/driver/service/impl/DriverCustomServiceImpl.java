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

import com.alibaba.fastjson.JSON;
import com.dc3.common.bean.driver.AttributeInfo;
import com.dc3.common.constant.Common;
import com.dc3.common.exception.ServiceException;
import com.dc3.common.model.Device;
import com.dc3.common.model.Point;
import com.dc3.common.sdk.bean.DriverContext;
import com.dc3.common.sdk.service.DriverCustomService;
import com.dc3.common.sdk.service.DriverService;
import com.dc3.driver.bean.Plcs7PointVariable;
import com.github.s7connector.api.S7Connector;
import com.github.s7connector.api.S7Serializer;
import com.github.s7connector.api.factory.S7ConnectorFactory;
import com.github.s7connector.api.factory.S7SerializerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.dc3.common.sdk.util.DriverUtils.attribute;
import static com.dc3.common.sdk.util.DriverUtils.value;

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

    /**
     * Plc Connector Map
     */
    private volatile Map<Long, S7Connector> s7ConnectorMap;

    @Override
    public void initial() {
        s7ConnectorMap = new ConcurrentHashMap<>(16);
    }

    @Override
    public String read(Map<String, AttributeInfo> driverInfo, Map<String, AttributeInfo> pointInfo, Device device, Point point) throws Exception {
        log.debug("Opc Da Read, device: {}, point: {}", JSON.toJSONString(device), JSON.toJSONString(point));
        S7Serializer serializer = getS7Serializer(device.getId(), driverInfo);
        Plcs7PointVariable plcs7PointVariable = getPointVariable(pointInfo, point.getType());
        return String.valueOf(serializer.dispense(plcs7PointVariable));
    }

    @Override
    public Boolean write(Map<String, AttributeInfo> driverInfo, Map<String, AttributeInfo> pointInfo, Device device, AttributeInfo value) throws Exception {
        log.debug("Opc Da Read, device: {}, value: {}", JSON.toJSONString(device), JSON.toJSONString(value));
        S7Serializer serializer = getS7Serializer(device.getId(), driverInfo);
        Plcs7PointVariable plcs7PointVariable = getPointVariable(pointInfo, value.getType());
        store(serializer, plcs7PointVariable, value.getType(), value.getValue());
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

    /**
     * ?????? plcs7 serializer
     * ????????????????????????????????????
     *
     * @param deviceId
     * @param driverInfo
     * @return
     */
    private S7Serializer getS7Serializer(Long deviceId, Map<String, AttributeInfo> driverInfo) {
        S7Connector s7Connector = s7ConnectorMap.get(deviceId);
        if (null == s7Connector) {
            log.debug("Plc S7 Connection Info {}", JSON.toJSONString(driverInfo));
            try {
                s7Connector = S7ConnectorFactory.buildTCPConnector().withHost(attribute(driverInfo, "host")).withPort(attribute(driverInfo, "port")).build();
            } catch (Exception e) {
                throw new ServiceException("new s7connector fail" + e.getMessage());
            }
        }
        if (null != s7Connector) {
            s7ConnectorMap.put(deviceId, s7Connector);
            return S7SerializerFactory.buildSerializer(s7Connector);
        }
        throw new ServiceException("new s7connector fail");
    }

    /**
     * ????????????????????????
     *
     * @param pointInfo
     * @return
     */
    private Plcs7PointVariable getPointVariable(Map<String, AttributeInfo> pointInfo, String type) {
        log.debug("Plc S7 Point Info {}", JSON.toJSONString(pointInfo));
        return new Plcs7PointVariable(attribute(pointInfo, "dbNum"), attribute(pointInfo, "byteOffset"), attribute(pointInfo, "bitOffset"), attribute(pointInfo, "blockSize"), type);
    }

    /**
     * ??? Plc S7 ?????????
     *
     * @param serializer
     * @param plcs7PointVariable
     * @param type
     * @param value
     */
    private void store(S7Serializer serializer, Plcs7PointVariable plcs7PointVariable, String type, String value) {
        switch (type.toLowerCase()) {
            case Common.ValueType.INT:
                int intValue = value(type, value);
                serializer.store(intValue, plcs7PointVariable.getDbNum(), plcs7PointVariable.getByteOffset());
                break;
            case Common.ValueType.LONG:
                long longValue = value(type, value);
                serializer.store(longValue, plcs7PointVariable.getDbNum(), plcs7PointVariable.getByteOffset());
                break;
            case Common.ValueType.FLOAT:
                float floatValue = value(type, value);
                serializer.store(floatValue, plcs7PointVariable.getDbNum(), plcs7PointVariable.getByteOffset());
                break;
            case Common.ValueType.DOUBLE:
                double doubleValue = value(type, value);
                serializer.store(doubleValue, plcs7PointVariable.getDbNum(), plcs7PointVariable.getByteOffset());
                break;
            case Common.ValueType.BOOLEAN:
                boolean booleanValue = value(type, value);
                serializer.store(booleanValue, plcs7PointVariable.getDbNum(), plcs7PointVariable.getByteOffset());
                break;
            case Common.ValueType.STRING:
                serializer.store(value, plcs7PointVariable.getDbNum(), plcs7PointVariable.getByteOffset());
                break;
            default:
                break;
        }
    }

}
