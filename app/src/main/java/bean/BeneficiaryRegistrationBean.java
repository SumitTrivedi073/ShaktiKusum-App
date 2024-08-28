package bean;

import java.io.Serializable;

public class BeneficiaryRegistrationBean implements Serializable {
    public String serialId,familyId,beneficiaryFormApplicantName,applicantFatherName,
            applicantMobile,applicantVillage,applicantBlock,applicantTehsil,
            applicantDistrict,pumpCapacity,applicantAccountNo,applicantIFSC,controllerType,pumpType,pumpAcDc,aadharNo;

    public BeneficiaryRegistrationBean() {
    }

    public BeneficiaryRegistrationBean(String serialId, String familyId, String beneficiaryFormApplicantName, String applicantFatherName, String applicantMobile, String applicantVillage, String applicantBlock, String applicantTehsil, String applicantDistrict, String pumpCapacity, String applicantAccountNo, String applicantIFSC, String controllerType, String pumpType, String pumpAcDc, String aadharNO) {
        this.serialId = serialId;
        this.familyId = familyId;
        this.beneficiaryFormApplicantName = beneficiaryFormApplicantName;
        this.applicantFatherName = applicantFatherName;
        this.applicantMobile = applicantMobile;
        this.applicantVillage = applicantVillage;
        this.applicantBlock = applicantBlock;
        this.applicantTehsil = applicantTehsil;
        this.applicantDistrict = applicantDistrict;
        this.pumpCapacity = pumpCapacity;
        this.applicantAccountNo = applicantAccountNo;
        this.applicantIFSC = applicantIFSC;
        this.controllerType = controllerType;
        this.pumpType = pumpType;
        this.pumpAcDc = pumpAcDc;
        this.aadharNo = aadharNO;
    }

    public String getSerialId() {
        return serialId;
    }

    public void setSerialId(String serialId) {
        this.serialId = serialId;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getBeneficiaryFormApplicantName() {
        return beneficiaryFormApplicantName;
    }

    public void setBeneficiaryFormApplicantName(String beneficiaryFormApplicantName) {
        this.beneficiaryFormApplicantName = beneficiaryFormApplicantName;
    }

    public String getApplicantFatherName() {
        return applicantFatherName;
    }

    public void setApplicantFatherName(String applicantFatherName) {
        this.applicantFatherName = applicantFatherName;
    }

    public String getApplicantMobile() {
        return applicantMobile;
    }

    public void setApplicantMobile(String applicantMobile) {
        this.applicantMobile = applicantMobile;
    }

    public String getApplicantVillage() {
        return applicantVillage;
    }

    public void setApplicantVillage(String applicantVillage) {
        this.applicantVillage = applicantVillage;
    }

    public String getApplicantBlock() {
        return applicantBlock;
    }

    public void setApplicantBlock(String applicantBlock) {
        this.applicantBlock = applicantBlock;
    }

    public String getApplicantTehsil() {
        return applicantTehsil;
    }

    public void setApplicantTehsil(String applicantTehsil) {
        this.applicantTehsil = applicantTehsil;
    }

    public String getApplicantDistrict() {
        return applicantDistrict;
    }

    public void setApplicantDistrict(String applicantDistrict) {
        this.applicantDistrict = applicantDistrict;
    }

    public String getPumpCapacity() {
        return pumpCapacity;
    }

    public void setPumpCapacity(String pumpCapacity) {
        this.pumpCapacity = pumpCapacity;
    }

    public String getApplicantAccountNo() {
        return applicantAccountNo;
    }

    public void setApplicantAccountNo(String applicantAccountNo) {
        this.applicantAccountNo = applicantAccountNo;
    }

    public String getApplicantIFSC() {
        return applicantIFSC;
    }

    public void setApplicantIFSC(String applicantIFSC) {
        this.applicantIFSC = applicantIFSC;
    }

    public String getControllerType() {
        return controllerType;
    }

    public void setControllerType(String controllerType) {
        this.controllerType = controllerType;
    }

    public String getPumpType() {
        return pumpType;
    }

    public void setPumpType(String pumpType) {
        this.pumpType = pumpType;
    }

    public String getPumpAcDc() {
        return pumpAcDc;
    }

    public void setPumpAcDc(String pumpAcDc) {
        this.pumpAcDc = pumpAcDc;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }
}
