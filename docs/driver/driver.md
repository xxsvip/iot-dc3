### 驱动模块介绍



> 在 `DC3` 中所谓的 **驱动**，主要意义在于 `主动获取（被动接收）设备数据` 以及 `控制设备`（`前提是设备支持被程序控制`），他实际上是一套程序逻辑（`主要借助Java实现`）；
>
> 把 `主动获取（被动接收）设备数据` 和 `控制` 不同类型设备的这套程序逻辑叫做 **驱动**，并把该套程序逻辑按照不同类型的设备进行归纳为不同的 **驱动模块**，从而有了 `dc3-driver-opc-da`、`dc3-driver-opc-ua`、`dc3-driver-mqtt`、`dc3-driver-modbus-tcp`、`dc3-driver-plcs7` 等。
>
> 上述描述的 **设备**，是一个宽泛的概念，把能接入到 `DC3` 中的 `手机`、`电脑`、`服务器`、`网关`、`硬件设备`甚至是某个`软件程序`等，都可以统称为 **设备** 。

