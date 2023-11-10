package ar.com.mbe;

import ar.com.mbe.core.common.H;
import ar.com.mbe.core.common.L;
import ar.com.mbe.sistema.seguridad.permiso.IPermisoService;
import ar.com.mbe.aperos.params.Parametro;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = MBEApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = {"classpath:application-test.properties"})
class MBEApplicationTests {
	private final ObjectMapper om = new ObjectMapper();

	@Autowired
	private IPermisoService permisoService;

	@Autowired
	private MockMvc mockMvc;

	public MBEApplicationTests() {
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		om.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
		om.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
		om.configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, false);
		om.registerModule(new JavaTimeModule());
	}

	public H login() throws Exception {
		var post = mockMvc.perform(post("/auth/login")
								   .accept(MediaType.ALL)
								   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
								   .header("Cache-Control", "no-cache")
								   .header("Authorization", "Basic NDM1Zjk5NjgtZjY0My00N2U5LTlkMTU6MjNhNWNiMzYxYjFk")
								   .content("username=jmfragueiro&password=fito")
						   )
						  .andExpect(status().isOk())
						  .andExpect(jsonPath("$.type").value("BEARER"))
						  .andReturn();
		L.info("---------[LOGIN OK]-------------------------------------------------");

		return om.readValue(post.getResponse().getContentAsString(), H.class);
	}

	public void logout(String jws) throws Exception {
		mockMvc.perform(post("/auth/logout")
								.accept(MediaType.ALL)
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)
								.header("Cache-Control", "no-cache")
								.header("Authorization", "Bearer "+jws)
			   )
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.type").value("BEARER"));
		L.info("---------[LOGOUT OK]------------------------------------------------");
	}

	@Test
	public void a_getParams() throws Exception {
		var jws = this.login().object();

		var result = mockMvc.perform(get("/params")
										   .accept(MediaType.ALL)
										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
										   .header("Cache-Control", "no-cache")
										   .header("Authorization", "Bearer " + jws)
										)
								.andExpect(status().isOk())
								.andReturn();

		List<Parametro> lista = om.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});

		L.info("---------[VALIDANDO PARAMETROS]-------------------------------------");
		assert lista.stream().sorted((p1, p2) -> Math.toIntExact(p1.getId() - p2.getId())).toList().get(2).getNombre().equals("ESTADOS CIVILES") : "el Nombre del Parametro 3 debe ser ESTADOS CIVILES!!";
		L.info("---------[FIN VALIDANDO PARAMETROS]---------------------------------");

		this.logout(jws);
	}

	@Test
	public void b_getParamType() throws Exception {
		var jws = this.login().object();

		L.info("---------[VALIDANDO PARAMETRO 3]------------------------------------");
		mockMvc.perform(get("/partyp/3")
								.accept(MediaType.ALL)
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)
								.header("Cache-Control", "no-cache")
								.header("Authorization", "Bearer " + jws)
			   )
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.nombre").value("ESTADOS CIVILES"))
			   .andDo(print())
		;
		L.info("---------[FIN VALIDANDO PARAMETRO 3]--------------------------------");

		this.logout(jws);
	}

	@Test
	public void c_changeParamType() throws Exception {
		var jws = this.login().object();

		L.info("---------[VALIDANDO PERMISO 4]--------------------------------------");
		var permiso = permisoService.findById(4L).orElseThrow();
		var nombre = permiso.getPermiso();
		var newnom = nombre.concat(" !!prueba!!");
		permiso.setPermiso(newnom);

		L.info("---------[MODIFICANDO PERMISO 4]------------------------------------");
		mockMvc.perform(put("/permisos")
								.accept(MediaType.ALL)
								.contentType(MediaType.APPLICATION_JSON)
								.header("Cache-Control", "no-cache")
								.header("Authorization", "Bearer " + jws)
								.content(om.writeValueAsString(permiso))
			   )
			   .andExpect(status().is2xxSuccessful())
		;

		L.info("---------[VERIFICANDO MODIFICACION PERMISO 4]-----------------------");
		mockMvc.perform(get("/permisos/4")
								.accept(MediaType.ALL)
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)
								.header("Cache-Control", "no-cache")
								.header("Authorization", "Bearer " + jws)
			   )
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.permiso").value(newnom))
		;

		L.info("---------[RESTAURANDO  PERMISO 4]------------------------------------");
		permiso.setPermiso(nombre);
		mockMvc.perform(put("/permisos")
								.accept(MediaType.ALL)
								.contentType(MediaType.APPLICATION_JSON)
								.header("Cache-Control", "no-cache")
								.header("Authorization", "Bearer " + jws)
								.content(om.writeValueAsString(permiso))
			   )
			   .andExpect(status().is2xxSuccessful())
		;

		L.info("---------[VERIFICANDO RESTAURACION  PERMISO 4]-----------------------");
		mockMvc.perform(get("/permisos/4")
								.accept(MediaType.ALL)
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)
								.header("Cache-Control", "no-cache")
								.header("Authorization", "Bearer " + jws)
			   )
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.permiso").value(nombre))
		;
		L.info("---------[FIN VALIDANDO  PERMISO 4]----------------------------------");

		this.logout(jws);
	}
}