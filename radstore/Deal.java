package radstore;

public class Deal {
    protected final float pid1;
    protected final float pid2;
    protected final float normalPrice;
    protected final float primePrice;
    protected final float elitePrice;
    protected final int dealId;
    protected static int counter = 0;

    public Deal(float pid1, float pid2, float normalPrice, float primePrice, float elitePrice) {
        this.pid1 = pid1;
        this.pid2 = pid2;
        this.normalPrice = normalPrice;
        this.primePrice = primePrice;
        this.elitePrice = elitePrice;
        this.dealId = ++counter;
    }

    public float getPid1() {
        return this.pid1;
    }

    public float getPid2() {
        return this.pid2;
    }

    public float getNormalPrice() {
        return this.normalPrice;
    }

    public float getPrimePrice() {
        return this.primePrice;
    }

    public float getElitePrice() {
        return this.elitePrice;
    }
}
