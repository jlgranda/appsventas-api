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
package org.jlgranda.appsventas.api.controller;

import net.tecnopro.util.Dates;
import org.jlgranda.appsventas.dto.PingData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jlgranda
 */
@RestController
@RequestMapping (path = "/ping")
public class Ping {
    
    
    @GetMapping
    public ResponseEntity ping() {
        String message = "appsventas-api::" + Dates.toString(Dates.now(), "E, dd MMM yyyy HH:mm:ss z");
        String description = "Appventas (c) 2022 v270720221224";
        return ResponseEntity.ok( new PingData(message, description, 1) );
    }
}
