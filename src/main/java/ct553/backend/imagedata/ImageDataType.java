package ct553.backend.imagedata;

public enum ImageDataType {
    
    PRODUCT(0),
    PRODUCT_DETAIL(1),
    AVATAR(2),
    REVIEW(3),
    POST(4);

    public final int type;

    ImageDataType(int type) {
        this.type = type;
    }

}
