package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
/** State entity class */
@Entity
@Table(name = "state")
@NamedQueries({
  @NamedQuery(
      name = "findStateByUUID",
      query = "Select s from StateEntity  s where s.uuid = :uuid"),
  @NamedQuery(name = "findAllStates", query = "Select s from StateEntity  s")
})

public class StateEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  private Integer Id;

  @Column(name = "uuid")
  @NotNull
  @Size(max = 200)
  private String uuid;

  @Column(name = "state_name")
  @Size(max = 30)
  private String stateName;

  public StateEntity() {}

  public StateEntity(@NotNull @Size(max = 200) String uuid, @Size(max = 30) String stateName) {
    this.uuid = uuid;
    this.stateName = stateName;
  }

  public Integer getId() {
    return Id;
  }

  public void setId(Integer id) {
    Id = id;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getStateName() {
    return stateName;
  }

  public void setStateName(String stateName) {
    this.stateName = stateName;
  }
    }
