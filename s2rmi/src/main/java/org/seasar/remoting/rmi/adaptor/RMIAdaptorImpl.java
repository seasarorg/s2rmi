package org.seasar.remoting.rmi.adaptor;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.seasar.extension.component.ComponentInvoker;
import org.seasar.remoting.rmi.filter.RMIFilter;

/**
 * RMIを使用してリモートメソッドの呼び出しを行うアダプタの実装クラス.
 * 
 * @author Kenichiro Murata
 */
public class RMIAdaptorImpl implements RMIAdaptor {

    private ComponentInvoker invoker;

    public List filters = new ArrayList();

    public Object invoke(final String componetName, final String methodName, final Object[] args)
            throws RemoteException, Exception {
        try {
            return new RMIFilterImpl().doFilter(componetName, methodName, args, null);
        }
        catch (final Exception e) {
            throw e;
        }
        catch (final Throwable t) {
            throw new Exception(t);
        }
    }

    /**
     * @param invoker
     *            invoker を設定。
     */
    public void setInvoker(final ComponentInvoker invoker) {
        this.invoker = invoker;
    }

    /**
     * RMIフィルタを返します。
     * 
     * @return RMIフィルタ
     */
    public synchronized RMIFilter[] getFilters() {
        return (RMIFilter[]) filters.toArray(new RMIFilter[filters.size()]);
    }

    /**
     * 複数のRMIフィルタを追加します。
     * 
     * @param filters
     *            RMIフィルタ
     */
    public synchronized void setFilters(final RMIFilter[] filters) {
        this.filters.addAll(Arrays.asList(filters));
    }

    /**
     * RMIフィルタを追加します。
     * 
     * @param filter
     *            RMIフィルタ
     */
    public synchronized void addFilter(final RMIFilter filter) {
        filters.add(filter);
    }

    /**
     * RMIフィルタチェーンをサポートするためのRMIフィルタ実装クラスです。
     * 
     * @author koichik
     * 
     */
    public class RMIFilterImpl implements RMIFilter {

        protected RMIFilter[] filters;
        protected int index;

        /**
         * インスタンスを構築します。
         * 
         */
        public RMIFilterImpl() {
            filters = getFilters();
        }

        public Object doFilter(final String componentName, final String methodName,
                final Object[] args, final RMIFilter filter) throws Throwable {
            if (index < filters.length) {
                return filters[index++].doFilter(componentName, methodName, args, this);
            }
            return invoker.invoke(componentName, methodName, args);
        }

    }
}
