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
package org.seasar.remoting.rmi.customizer;

import org.seasar.framework.container.ComponentCustomizer;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.InitMethodDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.impl.InitMethodDefImpl;
import org.seasar.framework.container.impl.PropertyDefImpl;
import org.seasar.remoting.rmi.connector.impl.RMIConnectorImpl;

/**
 * SMART deployにおいて{@link RMIConnectorImpl}のコンポーネント定義をカスタマイズするためのコンポーネントカスタマイザです。
 * 
 * @author koichik
 * 
 */
public class RMIConnectorCustomizer implements ComponentCustomizer {

    // constants
    protected static final String RMI_CONNECTOR_CLASS_NAME = "org.seasar.remoting.rmi.connector.impl.RMIConnectorImpl";

    // instance fields
    protected String baseURLAsString = "rmi://localhost:1108/";

    protected boolean lookupOnStartup;

    /**
     * ベースURLを文字列で設定します。
     * 
     * @param baseURLAsString
     *            ベースURLの文字列です
     */
    public void setBaseURLAsString(final String baseURLAsString) {
        this.baseURLAsString = baseURLAsString;
    }

    /**
     * 起動時にスタブをルックアップする場合は<code>true</code>を設定します。
     * 
     * @param lookupOnStartup
     *            起動時にスタブをルックアップする場合は<code>true</code>
     */
    public void setLookupOnStartup(final boolean lookupOnStartup) {
        this.lookupOnStartup = lookupOnStartup;
    }

    /**
     * コンポーネント定義をカスタマイズします。
     * <p>
     * {@link RMIConnectorImpl}のコンポーネント定義にベースURLを文字列で設定するための プロパティ定義を追加します。
     * <code>lookupOnStartup</code>プロパティが<code>true</code>に設定されている場合は
     * {@link RMIConnectorImpl#lookup}を起動するための初期化メソッドインジェクション定義を追加します。
     * </p>
     */
    public void customize(final ComponentDef componentDef) {
        if (componentDef.getComponentClass().getName().equals(RMI_CONNECTOR_CLASS_NAME)) {
            final PropertyDef propertyDef = new PropertyDefImpl("baseURLAsString", baseURLAsString);
            componentDef.addPropertyDef(propertyDef);
            if (lookupOnStartup) {
                final InitMethodDef initMethodDef = new InitMethodDefImpl("lookup");
                componentDef.addInitMethodDef(initMethodDef);
            }
        }
    }

}
