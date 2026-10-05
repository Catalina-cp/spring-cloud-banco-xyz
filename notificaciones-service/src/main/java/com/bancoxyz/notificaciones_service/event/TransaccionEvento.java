package com.bancoxyz.notificaciones_service.event;

import java.io.Serializable;
import java.util.List;

public class TransaccionEvento implements Serializable {

    private Long cuentaId;
    private String tipoEvento;
    private List<Integer> timestamp;

    public TransaccionEvento() {}

    public Long getCuentaId() { return cuentaId; }
    public void setCuentaId(Long cuentaId) { this.cuentaId = cuentaId; }

    public String getTipoEvento() { return tipoEvento; }
    public void setTipoEvento(String tipoEvento) { this.tipoEvento = tipoEvento; }

    public List<Integer> getTimestamp() { return timestamp; }
    public void setTimestamp(List<Integer> timestamp) { this.timestamp = timestamp; }
}