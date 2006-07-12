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
