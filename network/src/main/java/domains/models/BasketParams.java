package domains.models;

public class BasketParams {
    public Integer idProduct;
    public Integer count;

    public BasketParams(Integer count, Integer idProduct){
        this.count  = count;
        this.idProduct  = idProduct;
    }
}
