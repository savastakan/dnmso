package service;



import mslib.MassSpectrum;

import org.obolibrary.oboformat.model.Clause;
import org.obolibrary.oboformat.model.Frame;
import org.obolibrary.oboformat.model.Frame.FrameType;
import org.obolibrary.oboformat.model.FrameMergeException;
import org.obolibrary.oboformat.model.OBODoc;
import org.obolibrary.oboformat.parser.OBOFormatConstants.OboFormatTag;
import org.obolibrary.oboformat.parser.OBOFormatParser;
import org.obolibrary.oboformat.writer.OBOFormatWriter;

import domain.*;
import domain.DNMSO.Predictions;
import domain.Modification.ModAminoAcid;
import domain.Modification.Terminal;
import domain.Prediction.Sequence;
import domain.Prediction.Sequence.Gap;
import domain.Prediction.Sources;
import domain.Prediction.Sources.Source;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class DnmsoOboService  extends AbstractService{

	public String getServiceName() {
		return "DnmsoOboService";
	}
	private void addPropertyValue(Frame frame, String v1, String v2) {
		addPropertyValue(frame, v1, v2, null);
	}
	
	private void addPropertyValue(Frame frame, String v1, String v2, String v3) {
		Clause cl = new Clause(OboFormatTag.TAG_PROPERTY_VALUE);
		cl.addValue(v1);
		cl.addValue(v2);
		if (v3 != null) {
			cl.addValue(v3);
		}
		frame.addClause(cl);
	}

	public DNMSO read(){
        String path = getProperties().get(ServiceTag.PREDICTION_FILE_PATH.toString());
        
        DNMSO dnmso = (DNMSO)getContainer();
		if (dnmso == null){
			DnmsoFactory dnmsoFactory = new DnmsoFactory();
			dnmso = dnmsoFactory.createDnmso();
			
		}
		try {
			
			OBOFormatParser p = new OBOFormatParser();
		    OBODoc obodoc = p.parse(path);
			Frame dnmsoFrame = obodoc.getTermFrame("DNMSO:000000");
			
			for (Clause dmnsoClause :dnmsoFrame.getClauses("property_value")){
				if (dmnsoClause.getValue().equals("hasPredictions")){
					Predictions predictions = new Predictions();
					Frame predictionsFrame = obodoc.getTermFrame((String)dmnsoClause.getValue2());
					for (Clause predictionsClause : predictionsFrame.getClauses("property_value")){
						if (predictionsClause.getValue().equals("hasThreshold")){
							predictions.setThreshold(Double.parseDouble((String)predictionsClause.getValue2()));
						}
                        //software
                        if (predictionsClause.getValue().equals("hasSoftware")){
                            Frame softwareFrame = obodoc.getTermFrame((String)predictionsClause.getValue2());
                            Predictions.Software software = new Predictions.Software();
                            for (Clause softwareClause : softwareFrame.getClauses("property_value")) {
                                if (softwareClause.getValue().equals("hasSoftwareName")){
                                    software.setName((String)softwareClause.getValue2());
                                }
                                if (softwareClause.getValue().equals("hasSoftwareVersion")){
                                    software.setVersion((String) softwareClause.getValue2());
                                }
                                if (softwareClause.getValue().equals("hasPublication")){
                                    software.getPublication().add((String) softwareClause.getValue2());

                                }
                                if (softwareClause.getValue().equals("hasSettings")){
                                    software.setSettings(new Predictions.Software.Settings());
                                     Frame settingsFrame = obodoc.getTermFrame((String)softwareClause.getValue2());
                                     for (Clause settingsClause : settingsFrame.getClauses("property_value")){
                                         if (settingsClause.getValue().equals("hasSetting")){
                                             Predictions.Software.Settings.Setting setting = new Predictions.Software.Settings.Setting();
                                              Frame settingFrame = obodoc.getTermFrame((String)settingsClause.getValue2());
                                              for (Clause settingClause :settingFrame.getClauses("property_value")){
                                                  if (settingClause.getValue().equals("hasSettingName")){
                                                      setting.setName((String)settingClause.getValue2());
                                                  }
                                                  if (settingClause.getValue().equals("hasSettingValue")){
                                                      setting.setValue((String)settingClause.getValue2());
                                                  }
                                              }
                                             software.getSettings().getSetting().add(setting);
                                         }
                                     }

                                }

                            }
                            predictions.setSoftware(software);
                        }
                        //prediction
                        if (predictionsClause.getValue().equals("hasPrediction")){
							Prediction prediction =  new Prediction();
							
							Frame predictionFrame = obodoc.getTermFrame((String)predictionsClause.getValue2());
							for (Clause predictionClause : predictionFrame.getClauses("property_value")){
								if(predictionClause.getValue().equals("hasAssumedCharge")){
									
									if (predictionClause.getValue2()!=null){
										prediction.setAssumedCharge(Integer.valueOf((String)predictionClause.getValue2()));
									}
								}
                                if(predictionClause.getValue().equals("hasScores")){
                                    Frame scoresFrame = obodoc.getTermFrame((String)predictionClause.getValue2());
                                    for (Clause scoresClause : scoresFrame.getClauses("property_value")){
                                           if(scoresClause.getValue().equals("hasScore")){
                                               Prediction.Score score = new Prediction.Score();
                                               Frame scoreFrame = obodoc.getTermFrame((String)scoresClause.getValue2());
                                               for (Clause scoreClause : scoreFrame.getClauses("property_value")){
                                                   if(scoreClause.getValue().equals("hasScoreName")){
                                                       score.setName((String)scoreClause.getValue2());
                                                   }
                                                   if(scoreClause.getValue().equals("hasScoreValue")){
                                                       score.setValue((String)scoreClause.getValue2());
                                                   }
                                               }
                                               prediction.getScore().add(score);
                                           }
                                    }

                                }
								if (predictionClause.getValue().equals("hasSequence")){
									prediction.setSequence(new Sequence());
									Frame sequenceFrame = obodoc.getTermFrame((String)predictionClause.getValue2());
									for (Clause sequenceClause : sequenceFrame.getClauses("property_value")){
										if(sequenceClause.getValue().equals("hasCalculatedMass")){
											prediction.getSequence().setCalculatedMass(Double.parseDouble((String)sequenceClause.getValue2()));
										}
										if (sequenceClause.getValue().equals("hasConfidence")){
											prediction.getSequence().setConfidence(Double.parseDouble((String)sequenceClause.getValue2()));
										}
										if (sequenceClause.getValue().equals("hasSequence")){
											prediction.getSequence().setSequence((String)sequenceClause.getValue2());
										}
										if (sequenceClause.getValue().equals("hasSequenceElement")){
											Frame sequenceElementFrame = obodoc.getTermFrame((String)sequenceClause.getValue2());
											Clause sequenceElementNameClause = sequenceElementFrame.getClause(OboFormatTag.TAG_NAME);
											
											if (sequenceElementNameClause.getValue().equals("aminoAcid")){
												AminoAcid aminoAcid = new AminoAcid();
												for (Clause sequenceElementClause : sequenceElementFrame.getClauses("property_value")){
													if (sequenceElementClause.getValue().equals("hasCharacter")){
														aminoAcid.setCharacter(((String)sequenceElementClause.getValue2()));
													}
													if (sequenceElementClause.getValue().equals("hasConfidence")){
														aminoAcid.setConfidence((Double.parseDouble((String) sequenceElementClause.getValue2())));
													}
													if (sequenceElementClause.getValue().equals("hasPosition")){
														
														aminoAcid.setPos((Integer.valueOf((String) sequenceElementClause.getValue2())));
													}
													if (sequenceElementClause.getValue().equals("hasProof")){
														Proof proof = new Proof();
														Frame proofFrame = obodoc.getTermFrame((String)sequenceElementClause.getValue2());
														for (Clause proofClause : proofFrame.getClauses("property_value")){
															if (proofClause.getValue().equals("hasIntensity")){
																proof.setIntensity(Double.parseDouble((String) proofClause.getValue2()));
															}
															if (proofClause.getValue().equals("hasMz")){
																proof.setMz(Double.parseDouble((String) proofClause.getValue2()));
															}
															if (proofClause.getValue().equals("hasPosition")){
																proof.setPos(Integer.valueOf((String)proofClause.getValue2()));
																
															}
														}
														aminoAcid.getProof().add(proof);
													}
												}
                                                prediction.getSequence().getAminoAcidOrModifiedAminoAcidOrGap().add(aminoAcid);
											}
											if (sequenceElementNameClause.getValue().equals("gap")){
												Gap gap = new Gap();
												for (Clause sequenceElementClause : sequenceElementFrame.getClauses("property_value")){
													if (sequenceElementClause.getValue().equals("hasValue")){
														gap.setValue(((String)sequenceElementClause.getValue2()));
													}
													if (sequenceElementClause.getValue().equals("hasConfidence")){
														gap.setConfidence((Double.parseDouble((String) sequenceElementClause.getValue2())));
													}
													if (sequenceElementClause.getValue().equals("hasPosition")){
														gap.setPos((Integer.valueOf((String) sequenceElementClause.getValue2())));
													}
													if (sequenceElementClause.getValue().equals("hasProof")){
														Proof proof = new Proof();
														Frame proofFrame = obodoc.getTermFrame((String)sequenceElementClause.getValue2());
														for (Clause proofClause : proofFrame.getClauses("property_value")){
															if (proofClause.getValue().equals("hasIntensity")){
																proof.setIntensity(Double.parseDouble((String) proofClause.getValue2()));
															}
															if (proofClause.getValue().equals("hasMz")){
																proof.setMz(Double.parseDouble((String) proofClause.getValue2()));
															}
															if (proofClause.getValue().equals("hasPosition")){
																proof.setPos(Integer.valueOf((String)proofClause.getValue2()));
															}
														}
														gap.getProof().add(proof);
													}
													if (sequenceElementClause.getValue().equals("hasGapElement")){
														
														Frame gapElementFrame = obodoc.getTermFrame((String)sequenceElementClause.getValue2());
														Clause gapElementNameClause = gapElementFrame.getClause(OboFormatTag.TAG_NAME);
														
														if (gapElementNameClause.getValue().equals("aminoAcid")){
															AminoAcid aminoAcid = new AminoAcid();
															for (Clause gapElementClause : gapElementFrame.getClauses("property_value")){
																if (gapElementClause.getValue().equals("hasCharacter")){
																	aminoAcid.setCharacter(((String)gapElementClause.getValue2()));
																}
																if (gapElementClause.getValue().equals("hasConfidence")){
																	aminoAcid.setConfidence((Double.parseDouble((String) gapElementClause.getValue2())));
																}
																if (gapElementClause.getValue().equals("hasPosition")){
																	
																	aminoAcid.setPos((Integer.valueOf((String) gapElementClause.getValue2())));
																}
																if (gapElementClause.getValue().equals("hasProof")){
																	Proof proof = new Proof();
																	Frame proofFrame = obodoc.getTermFrame((String) gapElementClause.getValue2());
																	for (Clause proofClause : proofFrame.getClauses("property_value")){
																		if (proofClause.getValue().equals("hasIntensity")){
																			proof.setIntensity(Double.parseDouble((String) proofClause.getValue2()));
																		}
																		if (proofClause.getValue().equals("hasMz")){
																			proof.setMz(Double.parseDouble((String) proofClause.getValue2()));
																		}
																		if (proofClause.getValue().equals("hasPosition")){
																			proof.setPos(Integer.valueOf((String)proofClause.getValue2()));
																			
																		}
																	}
																	aminoAcid.getProof().add(proof);
																}
															}
			                                                gap.getAminoAcidOrModifiedAminoAcid().add(aminoAcid);
																
														} else if (gapElementNameClause.getValue().equals("modifiedAminoAcid")){
															ModifiedAminoAcid modifiedAminoAcid = new ModifiedAminoAcid();
															for (Clause gapElementClause : sequenceElementFrame.getClauses("property_value")){
																if (gapElementClause.getValue().equals("hasModificationName")){
																	modifiedAminoAcid.setModificationName(((String)gapElementClause.getValue2()));
																}
																if (gapElementClause.getValue().equals("hasConfidence")){
																	modifiedAminoAcid.setConfidence((Double.parseDouble((String) gapElementClause.getValue2())));
																}
																if (gapElementClause.getValue().equals("hasPosition")){
																	modifiedAminoAcid.setPos((Integer.valueOf((String) gapElementClause.getValue2())));
																}
																if (gapElementClause.getValue().equals("hasProof")){
																	Proof proof = new Proof();
																	Frame proofFrame = obodoc.getTermFrame((String)gapElementClause.getValue2());
																	for (Clause proofClause : proofFrame.getClauses("property_value")){
																		if (proofClause.getValue().equals("hasIntensity")){
																			proof.setIntensity(Double.parseDouble((String) proofClause.getValue2()));
																		}
																		if (proofClause.getValue().equals("hasMz")){
																			proof.setMz(Double.parseDouble((String) proofClause.getValue2()));
																		}
																		if (proofClause.getValue().equals("hasPosition")){
																			proof.setPos(Integer.valueOf((String)proofClause.getValue()));
																		}
																	}
																	modifiedAminoAcid.getProof().add(proof);
																}

																
															}
															 gap.getAminoAcidOrModifiedAminoAcid().add(modifiedAminoAcid);
														}
														
		                                                
													}

													
												}
                                                prediction.getSequence().getAminoAcidOrModifiedAminoAcidOrGap().add(gap);
											}
											if (sequenceElementNameClause.getValue().equals("modifiedAminoAcid")){
												ModifiedAminoAcid modifiedAminoAcid = new ModifiedAminoAcid();
												for (Clause sequenceElementClause : sequenceElementFrame.getClauses("property_value")){
													if (sequenceElementClause.getValue().equals("hasModificationName")){
														modifiedAminoAcid.setModificationName(((String)sequenceElementClause.getValue2()));
													}
													if (sequenceElementClause.getValue().equals("hasConfidence")){
														modifiedAminoAcid.setConfidence((Double.parseDouble((String) sequenceElementClause.getValue2())));
													}
													if (sequenceElementClause.getValue().equals("hasPosition")){
														modifiedAminoAcid.setPos((Integer.valueOf((String) sequenceElementClause.getValue2())));
													}
													if (sequenceElementClause.getValue().equals("hasProof")){
														Proof proof = new Proof();
														Frame proofFrame = obodoc.getTermFrame((String)sequenceElementClause.getValue2());
														for (Clause proofClause : proofFrame.getClauses("property_value")){
															if (proofClause.getValue().equals("hasIntensity")){
																proof.setIntensity(Double.parseDouble((String) proofClause.getValue2()));
															}
															if (proofClause.getValue().equals("hasMz")){
																proof.setMz(Double.parseDouble((String) proofClause.getValue2()));
															}
															if (proofClause.getValue().equals("hasPosition")){
																proof.setPos(Integer.valueOf((String)proofClause.getValue2()));
															}
														}
														modifiedAminoAcid.getProof().add(proof);
													}

													
												}
                                                prediction.getSequence().getAminoAcidOrModifiedAminoAcidOrGap().add(modifiedAminoAcid);
											}
											
										}
										
									}
								}

								if (predictionClause.getValue().equals("hasSources")){
									Sources sources = new Sources();
									
									Frame sourcesFrame = obodoc.getTermFrame((String) predictionClause.getValue2());
									for (Clause sourcesClause : sourcesFrame.getClauses("property_value") ){
										if (sourcesClause.getValue().equals("hasMerge")){
											sources.setMerge(Boolean.valueOf((String)sourcesClause.getValue2()));
										}
										if (sourcesClause.getValue().equals("hasSource")){
											Source source = new Source();
											Frame sourceFrame = obodoc.getTermFrame((String)sourcesClause.getValue2());
											for (Clause sourceClause : sourceFrame.getClauses("property_value")){
												if (sourceClause.getValue().equals("hasFileName")){
													source.setFileName((String)sourceClause.getValue2());
												}
												if (sourceClause.getValue().equals("hasScanId")){
													source.setScanId((String)sourceClause.getValue2());
												}
												
											}
											sources.getSource().add(source);
										}
									}
									prediction.getSources().add(sources);
									
								}
								
							}
							predictions.getPrediction().add(prediction);
						}
						
						
					}
					dnmso.getPredictions().add(predictions);
				}
				if (dmnsoClause.getValue().equals("hasModifications")){				
					Frame modificationsFrame = obodoc.getTermFrame((String)dmnsoClause.getValue2());
					for (Clause modificationsClause : modificationsFrame.getClauses("property_value")){
						if (modificationsClause.getValue().equals("hasModification")){
							Modification modification = new Modification();
							Frame modificationFrame = obodoc.getTermFrame((String)modificationsClause.getValue2());
							for (Clause modificationClause : modificationFrame.getClauses("property_value")){
								if (modificationClause.getValue().equals("hasMass")){
									modification.setMass(Double.valueOf((String)modificationClause.getValue2()));
								}
								if (modificationClause.getValue().equals("hasName")){
									modification.setName((String)modificationClause.getValue2());
								}
								if (modificationClause.getValue().equals("hasModAminoAcid")){
									ModAminoAcid modAminoAcid = new ModAminoAcid();
									Frame modAminoAcidFrame = obodoc.getTermFrame((String)modificationClause.getValue2());
									for(Clause modAminoAcidClause : modAminoAcidFrame.getClauses("property_value")){
										if (modAminoAcidClause.getValue().equals("hasAverageMass")){
											modAminoAcid.setAverageMass(Double.valueOf((String)modAminoAcidClause.getValue2()));
										}
										if (modAminoAcidClause.getValue().equals("hasCharacter")){
											modAminoAcid.setCharacter((String)modAminoAcidClause.getValue2());
										}
										if (modAminoAcidClause.getValue().equals("hasMonoIsotopicMass")){
											modAminoAcid.setMonoIsotopicMass(Double.valueOf((String)modAminoAcidClause.getValue2()));
										}
										
									}
									modification.setModAminoAcid(modAminoAcid);
								}
								if (modificationClause.getValue().equals("hasTerminal")){
									Terminal terminal = new Terminal();
									Frame terminalFrame = obodoc.getTermFrame((String)modificationClause.getValue2());
									for (Clause terminalClause : terminalFrame.getClauses("property_value")){
										if (terminalClause.getValue().equals("hasBack")){
											terminal.setBack((String)terminalClause.getValue2());
										}
										if (terminalClause.getValue().equals("hasFront")){
											terminal.setFront((String)terminalClause.getValue2());
										}
									}
									modification.setTerminal(terminal);
								}
							}
                            dnmso.getModifications().getModification().add(modification);
						}
						
						
					}
					
				}
				
				if (dmnsoClause.getValue().equals("hasSpectra")){
					
					Frame spectraFrame = obodoc.getTermFrame((String)dmnsoClause.getValue2());
					for (Clause spectraClause: spectraFrame.getClauses("property_value")){
						if (spectraClause.getValue().equals("hasSpectrum")){
							MassSpectrum spectrum = new MassSpectrum();
							Frame spectrumFrame = obodoc.getTermFrame((String)spectraClause.getValue2());
							for (Clause spectrumClause : spectrumFrame.getClauses("property_value")){
                                if(spectrumClause.getValue2()!=null && spectrumClause.getValue()!=null ){
                                    if(spectrumClause.getValue().equals("hasCsv")){
                                    	spectrum.addAll(convertCvsToPeak((String)spectrumClause.getValue2()));
                              
                                    }
                                    if(spectrumClause.getValue().equals("hasFileName")){
                                        spectrum.setFileName((String)spectrumClause.getValue2());
                                    }
                                    if(spectrumClause.getValue().equals("hasLink")){
                                        spectrum.setLink((String)spectrumClause.getValue2());
                                    }
                                    if(spectrumClause.getValue().equals("hasPrecursorIntensity")){
                                        spectrum.setPrecursorIntensity(Double.valueOf((String)spectrumClause.getValue2()));
                                    }
                                    if(spectrumClause.getValue().equals("hasPrecursorMZ")){
                                        spectrum.setPrecursorMass(Double.parseDouble((String)spectrumClause.getValue2()));
                                    }
                                    if(spectrumClause.getValue().equals("hasScanId")){
                                        spectrum.setScanNumber(Integer.parseInt((String) spectrumClause.getValue2()));
                                    }
                                }

							}
							dnmso.getSpectra().addSpectrum(spectrum);
						}
					}
				}
			}
			
		
		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dnmso;
	}
	public DNMSO write(){
        String path = getProperties().get(ServiceTag.OUTPUT.toString());
        DNMSO dnmso = (DNMSO)  getContainer();
		try {
			int index=0;
			NumberFormat formatter = new DecimalFormat("DNMSO:000000");
			OBODoc oboDoc = new OBODoc();
			
			Frame headerFrame = new Frame(FrameType.HEADER);
			headerFrame.addClause(new Clause(OboFormatTag.TAG_FORMAT_VERSION, "1.2"));
			headerFrame.addClause(new Clause(OboFormatTag.TAG_ONTOLOGY, "dnmso"));
			addPropertyValue(headerFrame, "http://www.iyte.edu.tr/dnmso", "DNMSO Ontology", "xsd:string");
			addPropertyValue(headerFrame, "defaultLanguage", "en", "xsd:string");
			oboDoc.setHeaderFrame(headerFrame);
		
			Frame dnmsoFrame = new Frame(FrameType.TERM);
			
			String dnmsoId = formatter.format(index);index++;
			dnmsoFrame.setId(dnmsoId);
			dnmsoFrame.addClause(new Clause(OboFormatTag.TAG_ID,dnmsoId));
			dnmsoFrame.addClause(new Clause(OboFormatTag.TAG_NAME, "dnmso"));
			//dnmsoFrame.addClause(new Clause("instance_of", "DNMSO"));
			

			//spectra
			Frame spectraFrame = new Frame(FrameType.TERM);
			String   spectraId = formatter.format(index);index++;
			spectraFrame.setId(spectraId);
			spectraFrame.addClause(new Clause(OboFormatTag.TAG_ID, spectraId));
			spectraFrame.addClause(new Clause(OboFormatTag.TAG_NAME, "spectra"));
			//spectraFrame.addClause(new Clause("instance_of", "Spectra"));
            addPropertyValue(dnmsoFrame, "hasSpectra", spectraId);

			for (MassSpectrum spectrum: dnmso.getSpectra().getSpectrum()){
				Frame spectrumFrame = new Frame(FrameType.TERM);
				String   spectrumId = formatter.format(index);index++;
				spectrumFrame.setId(spectrumId);
				spectrumFrame.addClause(new Clause(OboFormatTag.TAG_ID, spectrumId));
				spectrumFrame.addClause(new Clause(OboFormatTag.TAG_NAME, " spectrum"));
				//spectrumFrame.addClause(new Clause("instance_of", "Spectrum"));
                if(spectrum.getFileName()!=null){
                    addPropertyValue(spectrumFrame,"hasFileName",String.valueOf(spectrum.getFileName()),"xsd:string");
                }
				if (spectrum.getScanNumber()!=-1){
                    addPropertyValue(spectrumFrame,"hasScanId",String.valueOf(spectrum.getScanNumber()),"xsd:string");
                }
				if(spectrum.getSpectrum().size()>0){
                    addPropertyValue(spectrumFrame,"hasCsv",String.valueOf(convertPeakToCvs(spectrum.getSpectrum())),"xsd:string");
                }
				if(spectrum.getLink()!=null){
                    addPropertyValue(spectrumFrame,"hasLink",String.valueOf(spectrum.getLink()),"xsd:string");
                }
                 if(spectrum.getPrecursorMass()!=0D) {
                     addPropertyValue(spectrumFrame,"hasPrecursorMZ",String.valueOf(spectrum.getPrecursorMass()),"xsd:double");
                 }
                  if (spectrum.getPrecursorIntensity()!=0D){
                      addPropertyValue(spectrumFrame,"hasPrecursorIntensity",String.valueOf(spectrum.getPrecursorIntensity()),"xsd:double");
                  }
				addPropertyValue(spectraFrame,"hasSpectrum",spectrumId);
				oboDoc.addInstanceFrame(spectrumFrame);
			}
			
			
			oboDoc.addInstanceFrame(spectraFrame);
			
			
			//modifications
			Frame modificationsFrame = new Frame(FrameType.TERM);
			String  modificationsId = formatter.format(index);index++;
			modificationsFrame.setId(modificationsId);
			modificationsFrame.addClause(new Clause(OboFormatTag.TAG_ID, modificationsId));
			modificationsFrame.addClause(new Clause(OboFormatTag.TAG_NAME, "modifications"));
			//modificationsFrame.addClause(new Clause("instance_of", "Modifications"));
			addPropertyValue(dnmsoFrame, "hasModifications", modificationsId);
			
			for (Modification modification : dnmso.getModifications().getModification()){
				Frame modificationFrame = new Frame(FrameType.TERM);
				String  modificationId = formatter.format(index);index++;
				modificationFrame.setId(modificationId);
				modificationFrame.addClause(new Clause(OboFormatTag.TAG_ID, modificationId));
				modificationFrame.addClause(new Clause(OboFormatTag.TAG_NAME, "modification"));
				//modificationFrame.addClause(new Clause("instance_of", "Modification"));
                if(modification.getName()!=null){
                    addPropertyValue(modificationFrame,"hasName",String.valueOf(modification.getName()),"xsd:string");
                }
                if (modification.getMass()!=null){
                    addPropertyValue(modificationFrame,"hasMass",String.valueOf(modification.getMass()),"xsd:double");
                }

				
				//
				Frame modAminoAcidFrame = new Frame(FrameType.TERM);
				String  modAminoAcidId = formatter.format(index);index++;
				modAminoAcidFrame.setId(modAminoAcidId);
				modAminoAcidFrame.addClause(new Clause(OboFormatTag.TAG_ID, modAminoAcidId));
				modAminoAcidFrame.addClause(new Clause(OboFormatTag.TAG_NAME, "modAminoAcid"));
				//modAminoAcidFrame.addClause(new Clause("instance_of", "ModAminoAcid"));
                if(modification.getModAminoAcid().getCharacter()!=null){
                    addPropertyValue(modAminoAcidFrame,"hasCharacter",modification.getModAminoAcid().getCharacter(),"xsd:string");
                }
                if(modification.getModAminoAcid().getAverageMass()!=null){
                    addPropertyValue(modAminoAcidFrame,"hasAverageMass",String.valueOf(modification.getModAminoAcid().getAverageMass()),"xsd:double");
                }
                if(modification.getModAminoAcid().getMonoIsotopicMass()!=null){
                    addPropertyValue(modAminoAcidFrame,"hasMonoIsotopicMass",String.valueOf(modification.getModAminoAcid().getMonoIsotopicMass()),"xsd:double");
                }

				addPropertyValue(modificationFrame,"hasModAminoAcid",modAminoAcidId);
				oboDoc.addInstanceFrame(modAminoAcidFrame);

                if (modification.getTerminal()!=null){
                    Frame terminalFrame = new Frame(FrameType.TERM);
                    String  terminalId = formatter.format(index);index++;
                    terminalFrame.setId(terminalId);
                    terminalFrame.addClause(new Clause(OboFormatTag.TAG_ID, terminalId));
                    terminalFrame.addClause(new Clause(OboFormatTag.TAG_NAME, "terminal"));
                    //terminalFrame.addClause(new Clause("instance_of", "Terminal"));
                    if(modification.getTerminal().getBack()!=null){
                        addPropertyValue(terminalFrame,"hasBack",String.valueOf(modification.getTerminal().getBack()),"xsd:string");
                    }
                    if(modification.getTerminal().getFront()!=null){
                        addPropertyValue(terminalFrame,"hasFront",String.valueOf(modification.getTerminal().getFront()),"xsd:string");
                    }
                    addPropertyValue(modificationFrame,"hasTerminal",modAminoAcidId);
                    oboDoc.addInstanceFrame(terminalFrame);
                }

                addPropertyValue(modificationsFrame,"hasModification",modificationId);
				oboDoc.addInstanceFrame(modificationFrame);
			}
			oboDoc.addInstanceFrame(modificationsFrame);



			for (Predictions predictions : dnmso.getPredictions()){
				Frame predictionsFrame = new Frame(FrameType.TERM);
				String predictionsId = formatter.format(index);index++;
				predictionsFrame.setId(predictionsId);
				predictionsFrame.addClause(new Clause(OboFormatTag.TAG_ID, predictionsId));
				predictionsFrame.addClause(new Clause(OboFormatTag.TAG_NAME, "predictions"));
				if (predictions.getThreshold()!=null){
					addPropertyValue(predictionsFrame, "hasThreshold", String.valueOf(predictions.getThreshold()) ,"xsd:double");
				}
				
				addPropertyValue(dnmsoFrame, "hasPredictions", predictionsId);



                //software

				if (predictions.getSoftware()!=null){
					Frame softwareFrame = new Frame(FrameType.TERM);
	                String softwareId = formatter.format(index);index++;
	                softwareFrame.setId(softwareId);
	                softwareFrame.addClause(new Clause(OboFormatTag.TAG_ID, softwareId));
	                softwareFrame.addClause(new Clause(OboFormatTag.TAG_NAME, "software"));
	                if (predictions.getSoftware().getName()!=null) {
	                    addPropertyValue(softwareFrame, "hasSoftwareName",  predictions.getSoftware().getName(),"xsd:string");
	                }
	                if (predictions.getSoftware().getVersion()!=null){
	                    addPropertyValue(softwareFrame, "hasSoftwareVersion",  predictions.getSoftware().getVersion(),"xsd:string");
	                }

	                addPropertyValue(predictionsFrame,"hasSoftware",softwareId);
	                //settings
	                Frame settingsFrame = new Frame(FrameType.TERM);
	                String settingsId = formatter.format(index);index++;
	                settingsFrame.setId(settingsId);
	                settingsFrame.addClause(new Clause(OboFormatTag.TAG_ID, settingsId));
	                settingsFrame.addClause(new Clause(OboFormatTag.TAG_NAME, "settings"));

	                for (Predictions.Software.Settings.Setting setting :predictions.getSoftware().getSettings().getSetting()) {
	                    Frame settingFrame = new Frame(FrameType.TERM);
	                    String settingId = formatter.format(index);index++;
	                    settingFrame.setId(settingId);
	                    settingFrame.addClause(new Clause(OboFormatTag.TAG_ID, settingId));
	                    settingFrame.addClause(new Clause(OboFormatTag.TAG_NAME, "setting"));
	                    if(setting.getName()!=null){
	                        addPropertyValue(settingFrame, "hasSettingName",  setting.getName(),"xsd:string");
	                    }
	                    if(setting.getValue()!=null){
	                        addPropertyValue(settingFrame, "hasSettingValue",  setting.getValue(),"xsd:string");
	                    }

	                    addPropertyValue(settingsFrame, "hasSetting",  settingId);
	                    oboDoc.addTermFrame(settingFrame);
	                }
	                oboDoc.addTermFrame(settingsFrame);
	                for (String publication : predictions.getSoftware().getPublication()){
	                    if(publication!=null){
	                        addPropertyValue(softwareFrame, "hasPublication",  publication,"xsd:string");
	                    }

	                }

	                addPropertyValue(softwareFrame, "hasSettings", settingsId);
	                oboDoc.addTermFrame(softwareFrame);

				}
                



				for (Prediction prediction :predictions.getPrediction() ){

					Frame predictionFrame = new Frame(FrameType.TERM);
					String predictionId =formatter.format(index) ; index++;
					predictionFrame.setId(predictionId);
					predictionFrame.addClause(new Clause(OboFormatTag.TAG_ID, predictionId));
					predictionFrame.addClause(new Clause(OboFormatTag.TAG_NAME, "prediction"));
					//predictionFrame.addClause(new Clause("instance_of", "Prediction"));
					if (prediction.getAssumedCharge()!=null){
						addPropertyValue(predictionFrame,"hasAssumedCharge",String.valueOf(prediction.getAssumedCharge()),"xsd:double");
					}

                    //scores
                    Frame scoresFrame = new Frame(FrameType.TERM);
                    String scoresId =formatter.format(index) ; index++;
                    scoresFrame.setId(scoresId);
                    scoresFrame.addClause(new Clause(OboFormatTag.TAG_ID, scoresId));
                    scoresFrame.addClause(new Clause(OboFormatTag.TAG_NAME, "scores"));
                    addPropertyValue(predictionFrame,"hasScores",scoresId);
                    //score
                    for(Prediction.Score score : prediction.getScore()){
                        Frame scoreFrame = new Frame(FrameType.TERM);
                        String scoreId =formatter.format(index) ; index++;
                        scoreFrame.setId(scoreId);
                        scoreFrame.addClause(new Clause(OboFormatTag.TAG_ID, scoreId));
                        scoreFrame.addClause(new Clause(OboFormatTag.TAG_NAME, "score"));
                        if(score.getName()!=null)  {
                            addPropertyValue(scoreFrame,"hasScoreName",String.valueOf(score.getName()),"xsd:string");
                        }
                        if(score.getValue()!=null){
                            addPropertyValue(scoreFrame,"hasScoreValue",String.valueOf(score.getValue()),"xsd:string");
                        }
                        addPropertyValue(scoresFrame,"hasScore",scoreId);
                        oboDoc.addTermFrame(scoreFrame);
                    }
                    oboDoc.addTermFrame(scoresFrame);

                    for (Sources sources :prediction.getSources() ){
                    	//sources
    					Frame sourcesFrame =new Frame(FrameType.TERM);
    					String sourcesId =formatter.format(index) ; index++;
    					sourcesFrame.setId(sourcesId);
    					sourcesFrame.addClause(new Clause(OboFormatTag.TAG_ID, sourcesId));
    					sourcesFrame.addClause(new Clause(OboFormatTag.TAG_NAME, "sources"));
    					//sourcesFrame.addClause(new Clause("instance_of", "Source"));
    					addPropertyValue(predictionFrame,"hasSources",sourcesId);
    					if (sources.isMerge()!=null){
    						addPropertyValue(sourcesFrame,"hasMerge",String.valueOf(sources.isMerge()),"xsd:boolean");
    					} else {
                                addPropertyValue(sourcesFrame,"hasMerge","false","xsd:boolean");
                        }
                    	for (Source source : sources.getSource()){
    						Frame sourceFrame =new Frame(FrameType.TERM);
    						String sourceId =formatter.format(index) ; index++;
    						sourceFrame.setId(sourceId);
    						sourceFrame.addClause(new Clause(OboFormatTag.TAG_ID, sourceId));
    						sourceFrame.addClause(new Clause(OboFormatTag.TAG_NAME, "source"));
    						//sourceFrame.addClause(new Clause("instance_of", "Source"));
                            if(source.getFileName()!=null){
                                addPropertyValue(sourceFrame,"hasFileName",String.valueOf(source.getFileName()),"xsd:string");
                            }
    						if(source.getScanId()!=null){
                                addPropertyValue(sourceFrame,"hasScanId",String.valueOf(source.getScanId()),"xsd:string");
                            }

    						addPropertyValue(sourcesFrame,"hasSource",sourceId);
    						oboDoc.addInstanceFrame(sourceFrame);
    					}
    					oboDoc.addInstanceFrame(sourcesFrame);
                    }
					
					
					
					
					//sequence
					Frame sequenceFrame = new Frame(FrameType.TERM);
					String sequenceId = formatter.format(index);index++;
					sequenceFrame.setId(sequenceId);
					sequenceFrame.addClause(new Clause(OboFormatTag.TAG_ID, sequenceId));
					sequenceFrame.addClause(new Clause(OboFormatTag.TAG_NAME,"sequence"));
					//sequenceFrame.addClause(new Clause("instance_of", "Sequence"));
					if (prediction.getSequence().getSequence()!=null){
						addPropertyValue(sequenceFrame, "hasSequence", prediction.getSequence().getSequence(),"xsd:string");
					}
					if (prediction.getSequence().getCalculatedMass()!=null){
						addPropertyValue(sequenceFrame, "hasCalculatedMass", String.valueOf(prediction.getSequence().getCalculatedMass()),"xsd:double");
					}
					if (prediction.getSequence().getConfidence()!=null){
						addPropertyValue(sequenceFrame, "hasConfidence", String.valueOf(prediction.getSequence().getConfidence()),"xsd:double");
					}
					
					
					//sequence
					for (int k=0; k<prediction.getSequence().getAminoAcidOrModifiedAminoAcidOrGap().size();k++){
						if (prediction.getSequence().getAminoAcidOrModifiedAminoAcidOrGap().get(k) instanceof AminoAcid){
							String aminoAcidId = formatter.format(index);index++;
							Frame aminoAcidFrame = new Frame(FrameType.TERM);
							aminoAcidFrame.setId(aminoAcidId);
							aminoAcidFrame.addClause(new Clause(OboFormatTag.TAG_ID,aminoAcidId));
							aminoAcidFrame.addClause(new Clause(OboFormatTag.TAG_NAME, "aminoAcid"));
							//aminoAcidFrame.addClause(new Clause("instance_of", "AminoAcid"));
							AminoAcid aminoAcid = (AminoAcid)prediction.getSequence().getAminoAcidOrModifiedAminoAcidOrGap().get(k);
                            if(aminoAcid.getCharacter()!=null){
                                addPropertyValue(aminoAcidFrame, "hasCharacter", aminoAcid.getCharacter(),"xsd:string");
                            }
                            if(aminoAcid.getConfidence()!=null){
                                addPropertyValue(aminoAcidFrame, "hasConfidence", String.valueOf(aminoAcid.getConfidence()),"xsd:double");
                            }
                             if(aminoAcid.getPos()!=null){
                                 addPropertyValue(aminoAcidFrame, "hasPosition", String.valueOf(aminoAcid.getPos()),"xsd:integer");
                             }

							for (Proof proof : aminoAcid.getProof()){
								Frame proofFrame = new Frame(FrameType.TERM);
								String proofId = formatter.format(index);index++;
								proofFrame.setId(proofId);
								proofFrame.addClause(new Clause(OboFormatTag.TAG_ID,proofId));
								proofFrame.addClause(new Clause(OboFormatTag.TAG_NAME, "proof"));
								//proofFrame.addClause(new Clause("instance_of", "Proof"));
                                if(proof.getPos()!=null){
                                    addPropertyValue(proofFrame,"hasPosition",String.valueOf(proof.getPos()),"xsd:integer");
                                }
                                if(proof.getIntensity()!=null){
                                    addPropertyValue(proofFrame,"hasIntensity",String.valueOf(proof.getIntensity()),"xsd:double");
                                }
								if(proof.getMz()!=null){
                                    addPropertyValue(proofFrame,"hasMz",String.valueOf(proof.getMz()),"xsd:double");
                                }

								addPropertyValue(aminoAcidFrame,"hasProof",proofId);
								oboDoc.addInstanceFrame(proofFrame);
								
							}
							addPropertyValue(sequenceFrame, "hasSequenceElement", aminoAcidId);
							oboDoc.addInstanceFrame(aminoAcidFrame);
						}
						if (prediction.getSequence().getAminoAcidOrModifiedAminoAcidOrGap().get(k) instanceof ModifiedAminoAcid){
							ModifiedAminoAcid modifiedAminoAcid = (ModifiedAminoAcid)prediction.getSequence().getAminoAcidOrModifiedAminoAcidOrGap().get(k);
							Frame modifiedAminoAcidFrame = new Frame(FrameType.TERM);
							String modifiedAminoAcidId = formatter.format(index);index++;
							modifiedAminoAcidFrame.setId(modifiedAminoAcidId);
							modifiedAminoAcidFrame.addClause(new Clause(OboFormatTag.TAG_ID,modifiedAminoAcidId));
							modifiedAminoAcidFrame.addClause(new Clause(OboFormatTag.TAG_NAME, "modifiedAminoAcid"));
							//modifiedAminoAcidFrame.addClause(new Clause("instance_of", "ModifiedAminoAcid"));
                            if(modifiedAminoAcid.getConfidence()!=null){
                                addPropertyValue(modifiedAminoAcidFrame,"hasConfidence",String.valueOf(modifiedAminoAcid.getConfidence()),"xsd:double");
                            }
                            if(modifiedAminoAcid.getPos()!=null){
                                addPropertyValue(modifiedAminoAcidFrame,"hasPosition",String.valueOf(modifiedAminoAcid.getPos()),"xsd:integer");
                            }
							if(modifiedAminoAcid.getModificationName()!=null){
                                addPropertyValue(modifiedAminoAcidFrame,"hasModificationName",modifiedAminoAcid.getModificationName(),"xsd:string");
                            }

							for (Proof proof : modifiedAminoAcid.getProof()){
								Frame proofFrame = new Frame(FrameType.TERM);
								String proofId = formatter.format(index);index++;
								proofFrame.setId(proofId);
								proofFrame.addClause(new Clause(OboFormatTag.TAG_ID,proofId));
								proofFrame.addClause(new Clause(OboFormatTag.TAG_NAME, "proof"));
								//proofFrame.addClause(new Clause("instance_of", "Proof"));
                                if(proof.getPos()!=null){
                                    addPropertyValue(proofFrame,"hasPosition",String.valueOf(proof.getPos()),"xsd:integer");
                                }
								if(proof.getIntensity()!=null) {
                                    addPropertyValue(proofFrame,"hasIntensity",String.valueOf(proof.getIntensity()),"xsd:double");
                                }
                                if(proof.getMz()!=null){
                                    addPropertyValue(proofFrame,"hasMz",String.valueOf(proof.getMz()),"xsd:double");
                                }

								addPropertyValue(modifiedAminoAcidFrame,"hasProof",proofId);
								oboDoc.addInstanceFrame(proofFrame);
								
							}
							addPropertyValue(sequenceFrame, "hasSequenceElement", modifiedAminoAcidId);
							oboDoc.addInstanceFrame(modifiedAminoAcidFrame);
						}
						if (prediction.getSequence().getAminoAcidOrModifiedAminoAcidOrGap().get(k) instanceof Gap){
							Gap gap = (Gap) prediction.getSequence().getAminoAcidOrModifiedAminoAcidOrGap().get(k);
							Frame gapFrame = new Frame(FrameType.TERM);
							String gapId = formatter.format(index);index++;
							gapFrame.setId(gapId);
							gapFrame.addClause(new Clause(OboFormatTag.TAG_ID,gapId));
							gapFrame.addClause(new Clause(OboFormatTag.TAG_NAME, "gap"));
							//gapFrame.addClause(new Clause("instance_of", "Gap"));
                            if(gap.getPos()!=null){
                                addPropertyValue(gapFrame,"hasPosition",String.valueOf(gap.getPos()),"xsd:integer");
                            }
                            if(gap.getConfidence()!=null){
                                addPropertyValue(gapFrame,"hasConfidence",String.valueOf(gap.getConfidence()),"xsd:double");
                            }
							if(gap.getValue()!=null){
                                addPropertyValue(gapFrame,"hasValue",String.valueOf(gap.getValue()),"xsd:double");
                            }
						
							if (gap.getAminoAcidOrModifiedAminoAcid().size()>0){
								for (Object gapElement : gap.getAminoAcidOrModifiedAminoAcid()){
									if (gapElement instanceof AminoAcid){
										String aminoAcidId = formatter.format(index);index++;
										Frame aminoAcidFrame = new Frame(FrameType.TERM);
										aminoAcidFrame.setId(aminoAcidId);
										aminoAcidFrame.addClause(new Clause(OboFormatTag.TAG_ID,aminoAcidId));
										aminoAcidFrame.addClause(new Clause(OboFormatTag.TAG_NAME, "aminoAcid"));
										//aminoAcidFrame.addClause(new Clause("instance_of", "AminoAcid"));
										AminoAcid aminoAcid = (AminoAcid) gapElement;
			                            if(aminoAcid.getCharacter()!=null){
			                                addPropertyValue(aminoAcidFrame, "hasCharacter", aminoAcid.getCharacter(),"xsd:string");
			                            }
			                            if(aminoAcid.getConfidence()!=null){
			                                addPropertyValue(aminoAcidFrame, "hasConfidence", String.valueOf(aminoAcid.getConfidence()),"xsd:double");
			                            }
			                             if(aminoAcid.getPos()!=null){
			                                 addPropertyValue(aminoAcidFrame, "hasPosition", String.valueOf(aminoAcid.getPos()),"xsd:integer");
			                             }

										for (Proof proof : aminoAcid.getProof()){
											Frame proofFrame = new Frame(FrameType.TERM);
											String proofId = formatter.format(index);index++;
											proofFrame.setId(proofId);
											proofFrame.addClause(new Clause(OboFormatTag.TAG_ID,proofId));
											proofFrame.addClause(new Clause(OboFormatTag.TAG_NAME, "proof"));
											//proofFrame.addClause(new Clause("instance_of", "Proof"));
			                                if(proof.getPos()!=null){
			                                    addPropertyValue(proofFrame,"hasPosition",String.valueOf(proof.getPos()),"xsd:integer");
			                                }
			                                if(proof.getIntensity()!=null){
			                                    addPropertyValue(proofFrame,"hasIntensity",String.valueOf(proof.getIntensity()),"xsd:double");
			                                }
											if(proof.getMz()!=null){
			                                    addPropertyValue(proofFrame,"hasMz",String.valueOf(proof.getMz()),"xsd:double");
			                                }

											addPropertyValue(aminoAcidFrame,"hasProof",proofId);
											oboDoc.addInstanceFrame(proofFrame);
											
										}
										addPropertyValue(gapFrame, "hasGapElement", aminoAcidId);
										oboDoc.addInstanceFrame(aminoAcidFrame);
									} else if (gapElement instanceof ModifiedAminoAcid){
										ModifiedAminoAcid modifiedAminoAcid = (ModifiedAminoAcid) gapElement;
										Frame modifiedAminoAcidFrame = new Frame(FrameType.TERM);
										String modifiedAminoAcidId = formatter.format(index);index++;
										modifiedAminoAcidFrame.setId(modifiedAminoAcidId);
										modifiedAminoAcidFrame.addClause(new Clause(OboFormatTag.TAG_ID,modifiedAminoAcidId));
										modifiedAminoAcidFrame.addClause(new Clause(OboFormatTag.TAG_NAME, "modifiedAminoAcid"));
										//modifiedAminoAcidFrame.addClause(new Clause("instance_of", "ModifiedAminoAcid"));
			                            if(modifiedAminoAcid.getConfidence()!=null){
			                                addPropertyValue(modifiedAminoAcidFrame,"hasConfidence",String.valueOf(modifiedAminoAcid.getConfidence()),"xsd:double");
			                            }
			                            if(modifiedAminoAcid.getPos()!=null){
			                                addPropertyValue(modifiedAminoAcidFrame,"hasPosition",String.valueOf(modifiedAminoAcid.getPos()),"xsd:integer");
			                            }
										if(modifiedAminoAcid.getModificationName()!=null){
			                                addPropertyValue(modifiedAminoAcidFrame,"hasModificationName",modifiedAminoAcid.getModificationName(),"xsd:string");
			                            }

										for (Proof proof : modifiedAminoAcid.getProof()){
											Frame proofFrame = new Frame(FrameType.TERM);
											String proofId = formatter.format(index);index++;
											proofFrame.setId(proofId);
											proofFrame.addClause(new Clause(OboFormatTag.TAG_ID,proofId));
											proofFrame.addClause(new Clause(OboFormatTag.TAG_NAME, "proof"));
											//proofFrame.addClause(new Clause("instance_of", "Proof"));
			                                if(proof.getPos()!=null){
			                                    addPropertyValue(proofFrame,"hasPosition",String.valueOf(proof.getPos()),"xsd:integer");
			                                }
											if(proof.getIntensity()!=null) {
			                                    addPropertyValue(proofFrame,"hasIntensity",String.valueOf(proof.getIntensity()),"xsd:double");
			                                }
			                                if(proof.getMz()!=null){
			                                    addPropertyValue(proofFrame,"hasMz",String.valueOf(proof.getMz()),"xsd:double");
			                                }

											addPropertyValue(modifiedAminoAcidFrame,"hasProof",proofId);
											oboDoc.addInstanceFrame(proofFrame);
											
										}
										addPropertyValue(gapFrame, "hasGapElement", modifiedAminoAcidId);
										oboDoc.addInstanceFrame(modifiedAminoAcidFrame);
									}
								} 
								
							}

							for (Proof proof : gap.getProof()){
								Frame proofFrame = new Frame(FrameType.TERM);
								String proofId = formatter.format(index);index++;
								proofFrame.setId(proofId);
								proofFrame.addClause(new Clause(OboFormatTag.TAG_ID,proofId));
								proofFrame.addClause(new Clause(OboFormatTag.TAG_NAME, "proof"));
								//proofFrame.addClause(new Clause("instance_of", "Proof"));
                                if(proof.getPos()!=null){
                                    addPropertyValue(proofFrame,"hasPosition",String.valueOf(proof.getPos()),"xsd:integer");
                                }
								if(proof.getIntensity()!=null){
                                    addPropertyValue(proofFrame,"hasIntensity",String.valueOf(proof.getIntensity()),"xsd:double");
                                }
								if(proof.getMz()!=null){
                                    addPropertyValue(proofFrame,"hasMz",String.valueOf(proof.getMz()),"xsd:double");
                                }

								addPropertyValue(gapFrame,"hasProof",proofId);
								oboDoc.addInstanceFrame(proofFrame);
								
							}
							addPropertyValue(sequenceFrame, "hasSequenceElement", gapId);
							oboDoc.addInstanceFrame(gapFrame);
						}
	
					}
					oboDoc.addInstanceFrame(sequenceFrame);
					addPropertyValue(predictionFrame,"hasSequence",sequenceId);
					addPropertyValue(predictionsFrame,"hasPrediction",predictionId);
					oboDoc.addInstanceFrame(predictionFrame);
				}
				
				oboDoc.addInstanceFrame(predictionsFrame);
			}
			oboDoc.addInstanceFrame(dnmsoFrame);
            oboDoc.check();
			OBOFormatWriter writer = new OBOFormatWriter();
			StringWriter out = new StringWriter();
			BufferedWriter bufferedWriter = new BufferedWriter(out);
			
			writer.write(oboDoc,path);
			out.close();
			bufferedWriter.close();
			
		} catch (FrameMergeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return dnmso;

	}

	public Object run(Object container, String[] args) {
        processSettings(container,args);
		if (getProperties().get(ServiceTag.COMMAND.toString()).equals("write")) return write();
		if (getProperties().get(ServiceTag.COMMAND.toString()).equals("read")) return read();
        //if (getProperties().get(ServiceTag.COMMAND.toString()).equals("remove")) return remove((DNMSO)container,args[1],args[2],args[3]);
		return null;
	}
  /*
	private Object remove(DNMSO container, String string, String string2,
			String string3) {
		
		return null;
	}
	*/
	public boolean isValid(File file) {
		FileReader fileReader;
		try {
			fileReader = new FileReader(file);
			OBOFormatParser p = new OBOFormatParser();
			OBODoc obodoc = p.parse(new BufferedReader(fileReader));
			obodoc.check();
		} catch (Exception e) {
			return false;
		}
		return true;
	}


}
