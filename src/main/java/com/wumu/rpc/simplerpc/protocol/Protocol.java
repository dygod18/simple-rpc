package com.wumu.rpc.simplerpc.protocol;

import com.wumu.rpc.simplerpc.invoke.Invoker;

/**
 * Created by dydy on 2018/6/21.
 */
public interface Protocol {

    <T> Invoker<T> export(Invoker<T> invoker);

}
