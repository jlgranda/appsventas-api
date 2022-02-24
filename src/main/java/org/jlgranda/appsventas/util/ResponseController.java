package org.jlgranda.appsventas.util;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Service("responseController")
public class ResponseController {
	private Long id;
	private String estado;
}
