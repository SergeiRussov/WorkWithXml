package entity;

import lombok.Data;

@Data
public class Plant {

    private String common;
    private String botanical;
    private String zone;
    private String light;
    private String price;
    private long availability;
}
