/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.remoting.rmi.filter;

/**
 * 後続の{@link RMIFilter RMIフィルタ}を呼び出すためにS2RMIによって提供されるオブジェクトです。
 * 
 * @author koichik
 * 
 */
public interface RMIFilterChain {

    /**
     * 後続のフィルタ処理を実行します。
     * 
     * @param componentName
     *            リモートオブジェクトのコンポーネント名
     * @param methodName
     *            呼び出すメソッド名
     * @param args
     *            メソッドに与えられる引数
     * @return リモートメソッドの戻り値
     * @throws Throwable
     *             フィルタ処理で例外が発生した場合にスローされます。
     */
    Object doFilter(String componentName, String methodName, Object[] args) throws Throwable;

}
