public class Order {
    private String custID;
    private String prodID;
    private String trader;
    private String state;
    
    public Order(String cust, String trader, String prod, String state){
        if(cust == null || trader == null || prod == null || state == null)
            throw new NullPointerException();
        this.custID = cust;
        this.trader = trader;
        this.prodID = prod;
        this.state = state;
    }

    public String getCustID() {
        return this.custID;
    }

    public void setCustID(String custID) {
        this.custID = custID;
    }

    public String getProdID() {
        return this.prodID;
    }

    public void setProdID(String prodID) {
        this.prodID = prodID;
    }

    public String getTrader() {
        return this.trader;
    }

    public void setTrader(String trader) {
        this.trader = trader;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }
    
    /**
     * To change state of the order
     * @param state new state
     */
    public void changeState(String state){
        if(state == null)
            throw new NullPointerException();
        
        this.state = state;
    }

    @Override
    public String toString(){
        StringBuilder strBuild = new StringBuilder();

        strBuild.append(custID).append("%%").append(trader).append("%%").append(prodID).append("%%").append(state);
        return strBuild.toString();
    }
}
