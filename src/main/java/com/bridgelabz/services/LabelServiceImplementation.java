package com.bridgelabz.services;

import java.util.List;
import javax.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bridgelabz.exception.UserException;
import com.bridgelabz.model.Label;
import com.bridgelabz.model.LabelDto;
import com.bridgelabz.model.LabelUpdate;
import com.bridgelabz.model.Note;
import com.bridgelabz.model.User;
import com.bridgelabz.repository.IUserRepository;
import com.bridgelabz.repository.LabelRepositoryImplementation;
import com.bridgelabz.repository.NoteRepositoryImplementation;
import com.bridgelabz.util.JwtGenerator;

@Service
public class LabelServiceImplementation implements ILabelService {
	@Autowired
	private LabelRepositoryImplementation labelRepositoryImplementation;

	@Autowired
	ModelMapper modelMapper;

	private Label labelInformation = new Label();

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private JwtGenerator tokenGenerator;

	@Autowired
	private NoteRepositoryImplementation noteRepositoryImplementation;

	@Transactional
	@Override
	public void createLabel(LabelDto label, String token) {
		Long id = null;

		try {
			id = (long) tokenGenerator.parseJWT(token);
		} catch (Exception e) {

			throw new UserException("user does not exist");
		}

		User user = userRepository.getUserById(id);
		if (user != null) {
			Label labelInfo = labelRepositoryImplementation.fetchLabel(user.getUserId(), label.getName());
			if (labelInfo == null) {
				Label labell = new Label();

//				labelInformation = modelMapper.map(label, Label.class);
				BeanUtils.copyProperties(label, labell);
				labelInformation.getLabelId();
				labelInformation.getName();
				labelInformation.setUserId(user.getUserId());
				labelRepositoryImplementation.save(labelInformation);
			} else {
				throw new UserException("label with the given name is already present");
			}
		} else {
			throw new UserException("Note does not exist with the given id");
		}
	}

	@Transactional
	@Override
	public void createLabelAndMap(LabelDto label, String token, Long noteId) {
		Long id = null;

		try {
			id = (long) tokenGenerator.parseJWT(token);
		} catch (Exception e) {

			throw new UserException("user does not exist");
		}

		User user = userRepository.getUserById(id);
		if (user != null) {
			Label labelInfo = labelRepositoryImplementation.fetchLabel(user.getUserId(), label.getName());
			if (labelInfo == null) {
				BeanUtils.copyProperties(label, Label.class);
				labelInformation.setUserId(user.getUserId());
				labelRepositoryImplementation.save(labelInformation);
				Note note = noteRepositoryImplementation.findById(noteId);
				note.getList().add(labelInformation);
				noteRepositoryImplementation.saveNote(note);

			} else {
				throw new UserException("label with the given name is already present");
			}
		} else {
			throw new UserException("Note does not exist with the given id");
		}

	}

	@Transactional
	@Override
	public void addLabel(Long labelId, Long noteId, String token) {
		Note note = noteRepositoryImplementation.findById(noteId);
		Label label = labelRepositoryImplementation.fetchLabelById(labelId);
		label.getList().add(note);
		labelRepositoryImplementation.save(label);
	}

	@Transactional
	@Override
	public void removeLabel(Long labelId, Long noteId, String token) {
		Note note = noteRepositoryImplementation.findById(noteId);
		Label label = labelRepositoryImplementation.fetchLabelById(labelId);
		note.getList().remove(label);
		labelRepositoryImplementation.save(label);
	}

	@Transactional
	@Override
	public void editLabel(LabelUpdate label, String token) {
		Long id = null;

		try {
			id = (long) tokenGenerator.parseJWT(token);
		} catch (Exception e) {

			throw new UserException("user is not present ");
		}

		User user = userRepository.getUserById(id);
		if (user != null) {
			Label labelInfo = labelRepositoryImplementation.fetchLabelById(label.getLableId());
			if (labelInfo != null) {
				labelInfo.setName(label.getLabelName());
				labelRepositoryImplementation.save(labelInfo);
			} else {
				throw new UserException("label with the given id does not exist");
			}

		} else {
			throw new UserException("user does not exist");
		}

	}

	@Transactional
	@Override
	public void deleteLabel(LabelUpdate info, String token) {
		Long id = null;

		try {
			id = (long) tokenGenerator.parseJWT(token);
		} catch (Exception e) {

			throw new UserException("User does not exist");
		}

		User user = userRepository.getUserById(id);
		if (user != null) {
			Label labelInfo = labelRepositoryImplementation.fetchLabelById(info.getLableId());
			if (labelInfo != null) {
				labelRepositoryImplementation.deleteLabel(info.getLableId());
			} else {
				throw new UserException("Note does not exist");
			}
		}

	}

	@Override
	public List<Label> getLabel(String token) {

		Long id;
		try {
			id = (long) tokenGenerator.parseJWT(token);
		} catch (Exception e) {

			throw new UserException("note does not exist");
		}

		List<Label> labels = labelRepositoryImplementation.getAllLabel(id);
		return labels;

	}

	@Override
	public List<Note> getAllNotes(String token, Long labelId) {
		Label label = labelRepositoryImplementation.getLabel(labelId);
		List<Note> list = label.getList();
		System.out.println("label is" + list);

		return list;
	}
}