package ru.i_novus.integration.registry.backend;

import net.n2oapp.platform.test.autoconfigure.EnableEmbeddedPg;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.i_novus.integration.registry.backend.entity.ParticipantEntity;
import ru.i_novus.integration.registry.backend.entity.ParticipantMethodEntity;
import ru.i_novus.integration.registry.backend.entity.ParticipantPermissionEntity;
import ru.i_novus.integration.registry.backend.loader.ParticipantLoader;
import ru.i_novus.integration.registry.backend.loader.ParticipantMethodInfo;
import ru.i_novus.integration.registry.backend.loader.ParticipantMethodLoader;
import ru.i_novus.integration.registry.backend.model.IntegrationType;
import ru.i_novus.integration.registry.backend.model.Participant;
import ru.i_novus.integration.registry.backend.model.ParticipantPermission;
import ru.i_novus.integration.registry.backend.repository.ParticipantMethodRepository;
import ru.i_novus.integration.registry.backend.repository.ParticipantPermissionRepository;
import ru.i_novus.integration.registry.backend.repository.ParticipantRepository;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = RegistryBackendApplication.class)
@EnableEmbeddedPg
public class LoaderServerTest {

    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private ParticipantMethodRepository participantMethodRepository;
    @Autowired
    private ParticipantPermissionRepository participantPermissionRepository;


    @Autowired
    private ParticipantLoader participantLoader;

    @Autowired
    private ParticipantMethodLoader participantMethodLoader;

    @Test
    public void ParticipantMethodLoaderTest() {
        participantRepository.deleteAll();
        participantMethodRepository.deleteAll();
        participantPermissionRepository.deleteAll();

        List<Participant> participants = new ArrayList<>();
        Participant p1 = new Participant();
        Participant p2 = new Participant();
        participants.add(p1);
        participants.add(p2);
        p1.setCode("rdm");
        p1.setName("nsi");
        p1.setDisable(false);

        p2.setCode("default");
        p2.setName("def");
        p2.setDisable(false);

        participantLoader.load(participants, "");   //Загрузка участников
        assertThat(participantRepository.findAll().size(), is(2));


        List<ParticipantMethodInfo> data = new ArrayList<>();
        ParticipantMethodInfo method = new ParticipantMethodInfo();
        data.add(method);
        method.setPermissions(new ArrayList<>());
        method.setParticipantCode("rdm");
        method.setMethodCode("read");
        method.setUrl("test_url");
        method.setDisable(false);
        method.setIntegrationType(new IntegrationType());
        method.getIntegrationType().setId("REST_GET");
        method.getIntegrationType().setName("REST/GET");

        ParticipantPermission permission = new ParticipantPermission();
        method.getPermissions().add(permission);
        permission.setParticipantCode("default");
        permission.setSync(true);

        participantMethodLoader.load(data, "rdm");  //Загрузка метода

        List<ParticipantMethodEntity> methods = participantMethodRepository.findAll();
        List<ParticipantPermissionEntity> permissions = participantPermissionRepository.findAll();

        assertThat(methods.size(), is(1));
        assertThat(permissions.size(), is(1));
        Integer methodId = methods.get(0).getId();

        assertThat(methodId, is(permissions.get(0).getParticipantMethodId()));

        participantMethodLoader.load(data, "rdm"); //Повторная загрузка тех же данных.

        methods = participantMethodRepository.findAll();
        permissions = participantPermissionRepository.findAll();

        assertThat(methods.size(), is(1));
        assertThat(permissions.size(), is(1));
        assertThat(methodId, is(permissions.get(0).getParticipantMethodId()));
        assertThat(methodId, is(methods.get(0).getId()));
        assertThat(methods.get(0).getUrl(), is("test_url"));

        method.setUrl("newUrl");
        method.getPermissions().get(0).setCallbackUrl("newCallbackUrl");
        ParticipantPermission permissionTwo = new ParticipantPermission();
        method.getPermissions().add(permissionTwo);
        permissionTwo.setParticipantCode("rdm");
        permissionTwo.setSync(false);

        participantMethodLoader.load(data, "rdm"); //Загрузка измененных данных

        methods = participantMethodRepository.findAll();
        permissions = participantPermissionRepository.findAll();

        assertThat(methods.size(), is(1));
        assertThat(permissions.size(), is(2));
        assertThat(methodId, is(permissions.get(0).getParticipantMethodId()));
        assertThat(methodId, is(permissions.get(1).getParticipantMethodId()));
        assertThat(methodId, is(methods.get(0).getId()));
        assertThat(methods.get(0).getUrl(), is("newUrl"));

        method.setPermissions(null);
        participantMethodLoader.load(data, "rdm"); //Загрузка измененных данных (нет пермишенов)

        methods = participantMethodRepository.findAll();
        permissions = participantPermissionRepository.findAll();

        assertThat(methods.size(), is(1));
        assertThat(permissions.size(), is(0));

        participantMethodLoader.load(new ArrayList<>(), "rdm"); //Загрузка пустых данных

        methods = participantMethodRepository.findAll();
        permissions = participantPermissionRepository.findAll();

        assertThat(methods.size(), is(0));
        assertThat(permissions.size(), is(0));
    }


    @Test
    public void participantLoaderTest() {
        participantRepository.deleteAll();
        participantMethodRepository.deleteAll();
        participantPermissionRepository.deleteAll();

        List<Participant> data = new ArrayList<>();
        Participant p1 = new Participant();
        Participant p2 = new Participant();
        data.add(p1);
        data.add(p2);
        p1.setCode("rdm");
        p1.setName("nsi");
        p1.setDisable(false);

        p2.setCode("default");
        p2.setName("def");
        p2.setDisable(false);

        participantLoader.load(data, "");
        assertThat(participantRepository.findAll().size(), is(2));

        participantLoader.load(data, "");
        assertThat(participantRepository.findAll().size(), is(2));

        p1.setCode("rdm");
        p1.setName("nsiNEW");
        p1.setDisable(true);

        p2.setCode("defaultNew");
        p2.setName("defNew");
        p2.setDisable(true);

        participantLoader.load(data, "");
        assertThat(participantRepository.findAll().size(), is(3));

        ParticipantEntity part = participantRepository.findById("rdm").get();
        assertThat(part.getDisable(), is(true));
        assertThat(part.getName(), is("nsiNEW"));

        part = participantRepository.findById("default").get();
        assertThat(part.getDisable(), is(false));
        assertThat(part.getName(), is("def"));

        part = participantRepository.findById("defaultNew").get();
        assertThat(part.getDisable(), is(true));
        assertThat(part.getName(), is("defNew"));

        participantLoader.load(new ArrayList<>(), "");
        assertThat(participantRepository.findAll().size(), is(3));
    }


}
