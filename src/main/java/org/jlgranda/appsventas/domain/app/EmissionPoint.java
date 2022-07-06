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
package org.jlgranda.appsventas.domain.app;

import java.io.Serializable;
import javax.persistence.Column;
import org.jlgranda.appsventas.domain.DeletableObject;

/**
 *
 * @author usuario
 */
public class EmissionPoint extends DeletableObject<EmissionPoint> implements Serializable {
    
    @Column(name = "establishment_id", insertable = true, updatable = true, nullable = true)
    private Long establishmentId;
}
