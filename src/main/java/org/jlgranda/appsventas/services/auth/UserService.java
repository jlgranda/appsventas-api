/*
 * Licensed under the TECNOPRO License, Version 1.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.tecnopro.net/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jlgranda.appsventas.services.auth;

import org.jlgranda.appsventas.domain.Subject;
import org.jlgranda.appsventas.dto.UserModelData;
import org.jlgranda.appsventas.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import net.tecnopro.util.Dates;
import org.jlgranda.appsventas.domain.StatusType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jlgranda
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     *
     * @return
     */
    public UserRepository getUserRepository() {
        return userRepository;
    }

    public Optional<UserModelData> findByUsername(String username) {
        Optional<Subject> user = this.getUserRepository().findByUsername(username);
        UserModelData userData = new UserModelData();
        if (user.isPresent()) {
            BeanUtils.copyProperties(user.get(), userData);
            userData.setCodigoNombre(userData.getCode() + " - " + userData.getName());
            userData.setCode("" + user.get().getId());
            userData.setPersonId(user.get().getId());
            return Optional.of(userData);
        }
        return Optional.empty();
    }

    public Optional<UserModelData> findByCodigo(String codigo) {
        Optional<Subject> user = this.getUserRepository().findByCode(codigo);
        UserModelData userData = new UserModelData();
        if (user.isPresent()) {
            BeanUtils.copyProperties(user.get(), userData);
            userData.setCodigoNombre(userData.getCode() + " - " + userData.getName());
            userData.setCode("" + user.get().getId());
            userData.setPersonId(user.get().getId());
            return Optional.of(userData);
        }
        return Optional.empty();
    }

    public Optional<UserModelData> findById(Long id) {
        Optional<Subject> user = this.getUserRepository().findById(id);
        UserModelData userData = new UserModelData();
        if (user.isPresent()) {
            BeanUtils.copyProperties(user.get(), userData);
            userData.setCodigoNombre(userData.getCode() + " - " + userData.getName());
            userData.setCode("" + user.get().getId());
            return Optional.of(userData);
        }
        return Optional.of(userData);
    }

    public Optional<UserModelData> findByUUID(String uuid) {

        Optional<Subject> user = this.getUserRepository().findByUUID(uuid);

        Optional<UserModelData> result;
        if (user.isPresent()) {
            UserModelData userData = new UserModelData();
            BeanUtils.copyProperties(user.get(), userData);
            userData.setCodigoNombre(userData.getCode() + " - " + userData.getName());
            userData.setCode("" + user.get().getId());
            userData.setPersonId(user.get().getId());
            result = Optional.of(userData);

        } else {
            result = Optional.empty();
        }
        return result;
    }

    /**
     * Retorna datos de usuario para pantalla de administración de usuarios
     *
     * @param uuid
     * @return
     */
    public Optional<UserModelData> encontrarPorUUID(String uuid) {

        Optional<Subject> user = this.getUserRepository().findByUUID(uuid);

        Optional<UserModelData> result;
        if (user.isPresent()) {
            UserModelData userData = new UserModelData();
            BeanUtils.copyProperties(user.get(), userData);
            userData.setCodigoNombre(userData.getCode() + " - " + userData.getName());
            result = Optional.of(userData);

        } else {
            result = Optional.empty();
        }
        return result;
    }

    public Iterable<Subject> encontrarActivos() {
        return this.getUserRepository().encontrarActivos();
    }

    public Subject crearInstancia() {
        Subject _instance = new Subject();
        _instance.setActive(Boolean.TRUE);
        _instance.setDeleted(Boolean.FALSE);
        _instance.setLastUpdate(Dates.now());
        _instance.setCreatedOn(Dates.now());
        _instance.setStatus(StatusType.ACTIVE.toString());
        _instance.setActivationTime(Dates.now());
        _instance.setExpirationTime(Dates.addDays(Dates.now(), 364));
        _instance.setUuid(UUID.randomUUID().toString());
        return _instance;
    }
}
