import org.sql2o.*;
import java.util.List;

public class Text {
  private int id;
  private int panel_id;
  private int sequence;
  private String body;
  private String box_style;
  private String font;
  private String orientation;
  private String speaker;

  public Text(int panel_id, int sequence, String body, String orientation) {
    this.panel_id = panel_id;
    this.sequence = sequence;
    this.body = body;
    this.orientation = orientation;
  }

  public int getId() {
    return id;
  }

  public int getPanelId() {
    return panel_id;
  }

  public void setPanelId(int panel_id) {
    this.panel_id = panel_id;
  }

  public int getSequence() {
    return sequence;
  }

  public void setSequence(int sequence) {
    this.sequence = sequence;
  }

  public String getBody(){
    return body;
  }

  public void setBody(String body){
    this.body = body;
  }

  public String getOrientation(){
    return orientation;
  }

  public void setOrientation(String orientation){
    this.orientation = orientation;
  }

  public String getBoxStyle() {
    return box_style;
  }

  public void setBoxStyle(String box_style) {
    this.box_style = box_style;
  }

  public String getFont() {
    return font;
  }

  public void setFont(String font) {
    this.font = font;
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO texts (panel_id, sequence, body, orientation) VALUES (:panel_id, :sequence, :body, :orientation);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("panel_id", this.panel_id)
        .addParameter("sequence", this.sequence)
        .addParameter("body", this.body)
        .addParameter("orientation", this.orientation)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<Text> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM texts;";
      return con.createQuery(sql)
        .executeAndFetch(Text.class);
    }
  }

  public static Text find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM texts WHERE id = :id;";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Text.class);
    }
  }

  @Override
  public boolean equals(Object otherText) {
    if(!(otherText instanceof Text)) {
      return false;
    } else {
      Text newText = (Text) otherText;
      return this.getPanelId() == newText.getPanelId() &&
      this.getSequence() == newText.getSequence() &&
      this.getBody().equals(newText.getBody()) &&
      this.getOrientation().equals(newText.getOrientation());
    }
  }

  public void update() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE texts SET panel_id = :panel_id, sequence = :sequence, body = :body, orientation = :orientation WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("panel_id", this.panel_id)
        .addParameter("sequence", this.sequence)
        .addParameter("body", this.body)
        .addParameter("orientation", this.orientation)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String reSequence = "UPDATE texts SET sequence = sequence - 1 WHERE panel_id = :panel_id AND sequence > :sequence;";
      con.createQuery(reSequence)
        .addParameter("panel_id", this.panel_id)
        .addParameter("sequence", this.sequence)
        .executeUpdate();
      String delete = "DELETE FROM texts WHERE id = :id;";
      con.createQuery(delete)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }
}
