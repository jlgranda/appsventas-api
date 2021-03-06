/*
 * Copyright (C) 2022 usuario
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jlgranda.appsventas.dto.app;

import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author usuario
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class InvoiceGlobalData {

    @Deprecated
    List<InvoiceData> invoicesData = new ArrayList<>();
    @Deprecated
    List<InvoiceCountData> invoicesCountData = new ArrayList<>();
    
    List<InvoiceData> invoicesEmitidasData = new ArrayList<>();
    List<InvoiceCountData> invoicesEmitidasCountData = new ArrayList<>();
    
    List<InvoiceData> invoicesRecibidasData = new ArrayList<>();

}
