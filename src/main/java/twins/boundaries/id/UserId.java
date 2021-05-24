package twins.boundaries.id;


public class UserId {
    private String space;
    private String email;
    public static final String ID_SEPARATOR = ";";

    public UserId() {}

    public UserId(String space, String email) {
        this.space = space;
        this.email = email;
    }

    public String getSpace() {
        return this.space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getEmail() {
        return this.email;
    }

    public String convertToString()
    {
        return this.space
                .concat(ID_SEPARATOR)
                .concat(this.email);
    }
}
