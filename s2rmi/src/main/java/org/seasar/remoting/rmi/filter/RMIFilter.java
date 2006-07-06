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
     * 後続のフィルタに渡す<code>args</code>を変更することにより、リモートメソッドに渡される引数を変更することができます。
     * 後続のフィルタがすべて処理されると、リモートオブジェクトのメソッドが呼び出され、戻り値が返されます。 フィルタは戻り値を変更することもできます。
     * </p>
     * 
     * @param componentName
     *            リモートオブジェクトのコンポーネント名
     * @param methodName
     *            呼び出すメソッド名
     * @param args
     *            メソッドに与えられる引数
     * @param filter
     *            次に処理を行うフィルタ
     * @return リモートメソッドの戻り値
     * @throws Throwable
     */
    Object doFilter(String componentName, String methodName, Object[] args, RMIFilter filter)
            throws Throwable;

}
