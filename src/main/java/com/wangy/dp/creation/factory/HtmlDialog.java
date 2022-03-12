package com.wangy.dp.creation.factory;

/**
 * @author wangy
 * @version 1.0
 * @date 2022/3/12 / 17:31
 */
public class HtmlDialog extends Dialog{
    @Override
    Button createButton() {
        return new HtmlButton();
    }
}
