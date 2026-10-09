package com.bancoxyz.pagos_service.event;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TransaccionEvento implements Serializable {

    private Long cuentaId;
    private String tipoEvento;
    private LocalDateTime timestamp;

    public TransaccionEvento() {}

    public TransaccionEvento(Long cuentaId, String tipoEvento) {
        this.cuentaId = cuentaId;
        this.tipoEvento = tipoEvento;
        this.timestamp = LocalDateTime.now();
    }

    public Long getCuentaId() { return cuentaId; }
    public void setCuentaId(Long cuentaId) { this.cuentaId = cuentaId; }

    public String getTipoEvento() { return tipoEvento; }
    public void setTipoEvento(String tipoEvento) { this.tipoEvento = tipoEvento; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}