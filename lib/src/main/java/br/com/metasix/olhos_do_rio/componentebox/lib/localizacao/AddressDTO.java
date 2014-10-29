package br.com.metasix.olhos_do_rio.componentebox.lib.localizacao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.location.Address;

@SuppressWarnings("serial")
public class AddressDTO implements Serializable{
	
	private String endereco;
	private String numero;
	private String bairro;
	private String cidade;
	private String estado;
	private String cep;
	private String pais;
	private double latitude;
	private double longitude;
	
	public AddressDTO() {
		super();
	}
	public static AddressDTO fromAddressToAddressDTO(Address address){
		AddressDTO dto = new AddressDTO();
		if(address.getThoroughfare() != null &&
                !address.getThoroughfare().equals("")){
			dto.setEndereco(address.getThoroughfare());
		}
		if(address.getFeatureName() != null &&
                !address.getFeatureName().equals("")){
			dto.setNumero(address.getFeatureName());
		}
		if(address.getSubLocality() != null &&
                !address.getSubLocality().equals("")){
			dto.setBairro(address.getSubLocality());
		}
		if(address.getSubAdminArea() != null &&
                !address.getSubAdminArea().equals("")){
			dto.setCidade(address.getSubAdminArea());
		}
		if(address.getAdminArea() != null &&
                !address.getAdminArea().equals("")){
			dto.setEstado(address.getAdminArea());
		}
		if(address.getPostalCode() != null &&
                !address.getPostalCode().equals("")){
			dto.setCep(address.getPostalCode());
		}
		if(address.getCountryName() != null &&
                !address.getCountryName().equals("")){
			dto.setPais(address.getCountryName());
		}
		dto.setLatitude(address.getLatitude());
		dto.setLongitude(address.getLongitude());
		return dto;
	}
	public static List<AddressDTO> fromListAddressToListAddressDTO(List<Address> address){
		List<AddressDTO> list = new ArrayList<AddressDTO>();
		for (Address item : address) {
			list.add(fromAddressToAddressDTO(item));
		}
		return list;
	}
	public String getEndereco() {
		if(endereco == null){
			endereco = "";
		}
		return endereco;
	}
	public String getNumero() {
		return numero;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getBairro() {
        if(bairro == null){
            bairro = "";
        }
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getCep() {
        if(cep == null){
            cep = "";
        }
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	@Override
	public String toString() {
		return getEndereco() + " " +
				getBairro() + " " +
				getCep();
	}
}