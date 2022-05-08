package xyz.a5s7.candles.client;

import java.io.Serializable;
import java.lang.Double;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.List;

public class Tick implements Serializable {
  private List<? extends Data> data;

  private String type;

  public List<? extends Data> getData() {
    return this.data;
  }

  public void setData(List<? extends Data> data) {
    this.data = data;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public static class Data implements Serializable {
    private Double p;

    private String s;

    private Long t;

    private Integer v;

    public Data() {
    }

    public Data(Double p, String s, Long t, Integer v) {
      this.p = p;
      this.s = s;
      this.t = t;
      this.v = v;
    }

    public Double getP() {
      return this.p;
    }

    public void setP(Double p) {
      this.p = p;
    }

    public String getS() {
      return this.s;
    }

    public void setS(String s) {
      this.s = s;
    }

    public Long getT() {
      return this.t;
    }

    public void setT(Long t) {
      this.t = t;
    }

    public Integer getV() {
      return this.v;
    }

    public void setV(Integer v) {
      this.v = v;
    }

    @Override
    public String toString() {
      return "Data{" +
              "p=" + p +
              ", s='" + s + '\'' +
              ", t=" + t +
              ", v=" + v +
              '}';
    }
  }

  @Override
  public String toString() {
    return "Tick{" +
            "data=" + data +
            ", type='" + type + '\'' +
            '}';
  }
}
