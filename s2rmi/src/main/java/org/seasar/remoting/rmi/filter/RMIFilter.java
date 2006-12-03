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
package org.seasar.remoting.rmi.filter;

/**
 * RMI呼び出しの際に引数や戻り値を操作することのできるフィルタを定義します。
 * 
 * @author koichik
 */
public interface RMIFilter {

    /**
     * フィルタ処理を実行します。
     * <p>
     * 後続のフィルタチェーンに渡す<code>args</code>を変更することにより、リモートメソッドに渡される引数を変更することができます。
     * 後続のフィルタチェーンが処理されると、リモートオブジェクトのメソッドが呼び出され、戻り値が返されます。 フィルタは戻り値を変更することもできます。
     * </p>
     * 
     * @param componentName
     *            リモートオブジェクトのコンポーネント名
     * @param methodName
     *            呼び出すメソッド名
     * @param args
     *            メソッドに与えられる引数
     * @param chain
     *            後続の処理を行うフィルタのチェーン
     * @return リモートメソッドの戻り値
     * @throws Throwable
     *             フィルタ処理で例外が発生した場合にスローされます。
     */
    Object doFilter(String componentName, String methodName, Object[] args, RMIFilterChain chain)
            throws Throwable;

}
