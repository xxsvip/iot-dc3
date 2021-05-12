#!/bin/bash

#
#  Copyright 2016-2021 Pnoker. All Rights Reserved.
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
#

set -e

# start dc3 driver service
systemctl stop dc3-virtual
systemctl stop dc3-listening-virtual
systemctl stop dc3-opcda
systemctl stop dc3-opcua
systemctl stop dc3-modbus
systemctl stop dc3-mqtt
systemctl stop dc3-plcs7