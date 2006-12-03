/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.remoting.rmi.service.impl;

import org.seasar.remoting.rmi.server.Main;
import org.seasar.remoting.rmi.service.S2RmiService;

/**
 * S2RMIサーバプロセスを制御するサービスの実装です。
 * <p>
 * SMAPRT deployにより自動登録されるとクライアント側では不都合なため，冗長なクラス名にしています．
 * </p>
 * 
 * @author koichik
 */
public class S2RmiServiceImplementaion implements S2RmiService {

    public void stop() {
        Main.stop();
    }

}
