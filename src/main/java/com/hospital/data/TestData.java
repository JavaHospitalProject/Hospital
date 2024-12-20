package com.hospital.data;

public class TestData {
    public static final Object[][] TESTS = {
        {"T001", "Complete Blood Count (CBC)", "Laboratory", "1,200", "2 hours"},
        {"T002", "Blood Sugar Test", "Laboratory", "500", "1 hour"},
        {"T003", "Lipid Profile", "Laboratory", "2,000", "4 hours"},
        {"T004", "Chest X-Ray", "Radiology", "1,500", "30 minutes"},
        {"T005", "ECG", "Cardiology", "1,000", "30 minutes"},
        
        // Added more tests
        {"T006", "MRI Scan", "Radiology", "8,000", "1 hour"},
        {"T007", "CT Scan", "Radiology", "4,000", "45 minutes"},
        {"T008", "Ultrasound", "Radiology", "2,500", "30 minutes"},
        {"T009", "Hematology Test", "Laboratory", "1,800", "2 hours"},
        {"T010", "Thyroid Function Test", "Endocrinology", "1,500", "1 hour"},
        {"T011", "Liver Function Test", "Gastroenterology", "2,000", "2 hours"},
        {"T012", "Pregnancy Test", "Obstetrics", "500", "30 minutes"},
        {"T013", "Prostate Cancer Screening", "Urology", "3,000", "1 hour"},
        {"T014", "HIV Test", "Pathology", "1,000", "1 hour"},
        {"T015", "Hepatitis B Test", "Pathology", "1,200", "1 hour"},
        {"T016", "Hepatitis C Test", "Pathology", "1,200", "1 hour"},
        {"T017", "Tuberculosis Test", "Pathology", "1,500", "1 hour"},
        {"T018", "Blood Group Test", "Laboratory", "500", "1 hour"},
        {"T019", "Vitamin D Test", "Laboratory", "2,000", "1 hour"},
        {"T020", "Bone Marrow Test", "Hematology", "5,000", "2-3 hours"},
        {"T021", "Arthritis Test", "Rheumatology", "2,500", "1 hour"},
        {"T022", "Allergy Test", "Immunology", "3,000", "2 hours"},
        {"T023", "Cardiac Stress Test", "Cardiology", "5,500", "2 hours"},
        {"T024", "Electroencephalogram (EEG)", "Neurology", "3,000", "1 hour"},
        {"T025", "Electromyography (EMG)", "Neurology", "4,000", "1-2 hours"},
        {"T026", "Sleep Apnea Test", "Pulmonology", "6,000", "6 hours"},
        {"T027", "Colonoscopy", "Gastroenterology", "15,000", "2 hours"},
        {"T028", "Gastroscopy", "Gastroenterology", "12,000", "1 hour"},
        {"T029", "Endoscopy", "Gastroenterology", "10,000", "1 hour"},
        {"T030", "Cholesterol Test", "Cardiology", "1,000", "1 hour"},
        {"T031", "Echocardiogram", "Cardiology", "7,000", "1 hour"},
        {"T032", "Blood Culture", "Microbiology", "3,000", "2 hours"},
        {"T033", "Urinalysis", "Pathology", "500", "1 hour"},
        {"T034", "Cytology Test", "Pathology", "2,500", "2 hours"},
        {"T035", "Biopsy", "Oncology", "15,000", "1-2 hours"},
        {"T036", "Skin Allergy Test", "Dermatology", "2,000", "1 hour"},
        {"T037", "Hearing Test", "Otolaryngology", "2,000", "1 hour"},
        {"T038", "Vision Test", "Ophthalmology", "1,000", "30 minutes"},
        {"T039", "Diabetes Test", "Endocrinology", "1,000", "1 hour"},
        {"T040", "Blood Pressure Test", "Cardiology", "300", "10 minutes"},
        {"T041", "Glucose Tolerance Test", "Endocrinology", "1,800", "2 hours"},
        {"T042", "Cervical Cancer Screening", "Gynecology", "4,000", "1 hour"},
        {"T043", "Urine Culture", "Microbiology", "1,200", "1-2 hours"},
        {"T044", "Hearing Test for Newborn", "Pediatrics", "1,500", "30 minutes"},
        {"T045", "Pap Smear Test", "Gynecology", "3,000", "1 hour"},
        {"T046", "Fertility Test", "Gynecology", "6,000", "2-3 hours"},
        {"T047", "Sperm Count Test", "Urology", "2,500", "1 hour"},
        {"T048", "Bone Density Scan", "Radiology", "3,500", "1 hour"},
        {"T049", "Mammogram", "Radiology", "4,000", "45 minutes"},
        {"T050", "Stress Test", "Cardiology", "5,000", "2 hours"},
        
        // Added more tests
        {"T051", "Genetic Testing", "Genetics", "7,000", "2-3 hours"},
        {"T052", "Chlamydia Test", "Pathology", "2,000", "1 hour"},
        {"T053", "Gonorrhea Test", "Pathology", "2,000", "1 hour"},
        {"T054", "Zika Virus Test", "Pathology", "2,500", "1 hour"},
        {"T055", "Chronic Kidney Disease Test", "Nephrology", "4,000", "1 hour"},
        {"T056", "Pancreatic Enzyme Test", "Gastroenterology", "3,000", "1 hour"},
        {"T057", "Lung Function Test", "Pulmonology", "3,500", "1 hour"},
        {"T058", "Liver Biopsy", "Gastroenterology", "18,000", "2-3 hours"},
        {"T059", "Cytogenetic Test", "Genetics", "10,000", "2 hours"},
        {"T060", "Paternity Test", "Pathology", "8,000", "1-2 hours"}
    };

    public static final String[] TEST_COLUMNS = {
        "Test ID", "Test Name", "Department", "Price (Tk)", "Duration"
    };

    public static final String[] TEST_CATEGORIES = {
        "All Tests",
        "Laboratory",
        "Radiology",
        "Cardiology",
        "Neurology",
        "Pathology",
        "Gastroenterology",
        "Urology",
        "Oncology",
        "Gynecology",
        "Pulmonology",
        "Ophthalmology",
        "Dermatology",
        "Microbiology",
        "Genetics",
        "Immunology",
        "Endocrinology"
    };
}
