package service.medical;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicalServiceImplTest {

    private static PatientInfo patientSemen;
    private static PatientInfo patientIvan;
    private final String WARNING_MES_FRMT = "Warning, patient with id: %s, need help";

    @BeforeAll
    public static void setUpPatients() {
        patientSemen = new PatientInfo("semen", "Семен", "Михайлов", LocalDate.of(1982, 1, 16),
                new HealthInfo(new BigDecimal("36.6"), new BloodPressure(125, 78)));

        patientIvan = new PatientInfo("ivan", "Иван", "Петров", LocalDate.of(1980, 11, 26),
                new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80)));
    }

    @Test
    void checkBloodPressureSendAlert() {
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        SendAlertService alertService = Mockito.mock(SendAlertService.class);

        Mockito.when(patientInfoRepository.getById(patientSemen.getId())).thenReturn(patientSemen);
        String message = String.format(WARNING_MES_FRMT, patientSemen.getId());

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);
        medicalService.checkBloodPressure(patientSemen.getId(), new BloodPressure(140, 97));

        Mockito.verify(alertService, Mockito.times(1)).send(message);
    }

    @Test
    void checkBloodPressureNormalValue() {
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        SendAlertService alertService = Mockito.mock(SendAlertService.class);

        Mockito.when(patientInfoRepository.getById(patientSemen.getId())).thenReturn(patientSemen);
        String message = String.format(WARNING_MES_FRMT, patientSemen.getId());

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);
        medicalService.checkBloodPressure(patientSemen.getId(), new BloodPressure(125, 78));

        Mockito.verify(alertService, Mockito.times(0)).send(message);
    }

    @Test
    void checkTemperatureSendAlert() {
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        SendAlertService alertService = Mockito.mock(SendAlertService.class);

        Mockito.when(patientInfoRepository.getById(patientIvan.getId())).thenReturn(patientIvan);
        String message = String.format(WARNING_MES_FRMT, patientIvan.getId());

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);
        medicalService.checkTemperature(patientIvan.getId(), new BigDecimal("10.20"));

        Mockito.verify(alertService, Mockito.times(1)).send(message);
    }

    @Test
    void checkTemperatureNormalValue() {
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        SendAlertService alertService = Mockito.mock(SendAlertService.class);

        Mockito.when(patientInfoRepository.getById(patientIvan.getId())).thenReturn(patientIvan);
        String message = String.format(WARNING_MES_FRMT, patientIvan.getId());

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);
        medicalService.checkTemperature(patientIvan.getId(), new BigDecimal("36.65"));

        Mockito.verify(alertService, Mockito.times(0)).send(message);
    }

}
