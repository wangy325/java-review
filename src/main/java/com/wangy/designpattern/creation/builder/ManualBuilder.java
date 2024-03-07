package com.wangy.designpattern.creation.builder;

/**
 * @author wangy
 * @version 1.0
 * @date 2022/3/13 / 22:00
 */
public class ManualBuilder implements Builder{

    private Manual manual;
    private Product product;

    // 一个新的生成器实例必须包含一个在后续组装过程中使用的空产品对象
    public ManualBuilder() {
        this.reset();
    }

    @Override
    public void reset() {
        this.manual = new Manual();

    }

    @Override
    public void setSeats(int seats) {
        System.out.println("");
    }

    @Override
    public void setEngine(Engine engine) {

    }

    @Override
    public void setTripComputer(boolean computer) {

    }

    @Override
    public void setGPS(boolean gps) {

    }

    // 具体生成器需要自行提供获取结果的方法。这是因为不同类型的生成器可能
    // 会创建不遵循相同接口的、完全不同的产品。所以也就无法在生成器接口中
    // 声明这些方法（至少在静态类型的编程语言中是这样的）。
    //
    // 通常在生成器实例将结果返回给客户端后，它们应该做好生成另一个产品的
    // 准备。因此生成器实例通常会在 `getProduct（获取产品）`方法主体末尾
    // 调用重置方法。但是该行为并不是必需的，你也可让生成器等待客户端明确
    // 调用重置方法后再去处理之前的结果。
    @Override
    public Manual getProduct() {
        this.product = this.manual;
        this.reset();
        return (Manual) product;
    }
}
