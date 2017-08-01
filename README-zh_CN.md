# NetMan

[简体中文](README-zh_CN.md) | [English](README.md)

`NetMan` 是用来阻止以下程序联网的一个工具：
- 总有本不需要联网的应用去请求联网，耗费掉不必要的流量
- 你不想让某个应用联网（与上一条有部分重复）
- 让某些流量消耗较大的程序仅WIFI时可以联网
- ...

**注意，因为本程序使用的 `iptables` ，因此需要 `root` 权限！**

# License

```License
Copyright 2017 PinkD

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```