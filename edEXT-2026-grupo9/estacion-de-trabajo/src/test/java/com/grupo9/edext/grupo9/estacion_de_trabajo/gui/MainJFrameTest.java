package com.grupo9.edext.grupo9.estacion_de_trabajo.gui;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MainJFrameTest {

    @Test
    public void testInstanciacionMainJFrame() {
        assertDoesNotThrow(() -> {
            MainJFrame main = new MainJFrame();
            assertNotNull(main);
        });
    }
}
