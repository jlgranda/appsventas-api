/*
 * Copyright (C) 2022 jlgranda
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
package org.jlgranda.appsventas.services.auth;

import org.jlgranda.appsventas.domain.UsersRoles;
import org.jlgranda.appsventas.repository.auth.usersRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jlgranda
 */
@Service
public class UsersRolesService {

    @Autowired
    private usersRolesRepository repository;

    public usersRolesRepository getRepository() {
        return repository;
    }

    public UsersRoles guardar(UsersRoles usersRoles) {
        return this.getRepository().save(usersRoles);
    }

}
