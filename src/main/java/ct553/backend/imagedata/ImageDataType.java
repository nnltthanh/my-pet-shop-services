package ct553.backend.imagedata;

public enum ImageDataType {
    
    PRODUCT(0),
    PRODUCT_DETAIL(1),
    AVATAR(2),
    POST(3);

    public final int type;

    private ImageDataType(int type) {
        this.type = type;
    }

}
